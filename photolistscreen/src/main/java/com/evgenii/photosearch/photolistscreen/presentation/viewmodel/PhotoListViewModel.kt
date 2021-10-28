package com.evgenii.photosearch.photolistscreen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.evgenii.photosearch.core.presentation.model.Event
import com.evgenii.photosearch.photolistscreen.R
import com.evgenii.photosearch.photolistscreen.domain.usecases.PhotoListUseCase
import com.evgenii.photosearch.photolistscreen.presentation.mapper.PhotoItemMapper
import com.evgenii.photosearch.photolistscreen.presentation.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val mapper: PhotoItemMapper,
    private val photoListUseCase: PhotoListUseCase
) : ViewModel() {

    private var userQuery: String = EMPTY_QUERY

    private var _photoListFlow: Flow<PagingData<PhotoItem>>? = null
    val photoListFlow: Flow<PagingData<PhotoItem>>?
        get() = _photoListFlow

    private var _photoListUpdated = MutableLiveData<Unit>()
    val photoListUpdated: LiveData<Unit> = _photoListUpdated

    private val _screenState: MutableLiveData<PhotoListScreenState> =
        MutableLiveData(PhotoListScreenState())
    val screenState: LiveData<PhotoListScreenState> = _screenState

    private val _commands: MutableLiveData<Event<Commands>> = MutableLiveData()
    val commands: LiveData<Event<Commands>> = _commands

    fun onSearchTextChanged(query: String) {
        userQuery = query
    }

    fun onSearchButtonClick() =
        searchPhotos()

    fun onPhotoItemClick(photoItem: PhotoItem) {
        _commands.value = Event(ShowDetail(photoItem))
    }

    fun onRetryButtonClick() =
        searchPhotos()

    fun onLoadStateListener(loadState: CombinedLoadStates, itemCount: Int) {
        val refreshState = loadState.refresh
        if (refreshState.isNotLoadingProcess()) {
            when {
                refreshState.isLoadError() -> {
                    _screenState.value = PhotoListScreenState(
                        isErrorTextVisible = true,
                        isRetryButtonVisible = true,
                        errorTextResId = R.string.error_load_description
                    )
                    Timber.d((refreshState as LoadState.Error).error.localizedMessage)
                }
                loadState.isLoadEmpty(itemCount) -> {
                    _screenState.value = PhotoListScreenState(
                        isErrorTextVisible = true,
                        errorTextResId = R.string.error_empty_result
                    )
                }
                itemCount > 0 -> {
                    _screenState.value = PhotoListScreenState(
                        isPhotoListVisible = true,
                    )
                }
            }
        }
    }

    private fun searchPhotos() {
        _photoListFlow = photoListUseCase.getPhotos(userQuery)
            .cachedIn(viewModelScope).map { pagingData ->
                pagingData.map { photo ->
                    mapper.mapPhotoToPhotoItem(photo)
                }
            }
        _photoListUpdated.value = Unit
        _commands.value = Event(HideKeyboard)
        _screenState.value = PhotoListScreenState(
            isPhotoListVisible = true,
            isLoadingProgressBarVisible = true
        )
    }

    private fun LoadState.isLoadError(): Boolean =
        this is LoadState.Error

    private fun CombinedLoadStates.isLoadEmpty(itemCount: Int): Boolean =
        source.refresh is LoadState.NotLoading &&
                append.endOfPaginationReached && itemCount == 0

    private fun LoadState.isNotLoadingProcess(): Boolean =
        this !is LoadState.Loading

    companion object {
        private const val EMPTY_QUERY = ""
    }
}
