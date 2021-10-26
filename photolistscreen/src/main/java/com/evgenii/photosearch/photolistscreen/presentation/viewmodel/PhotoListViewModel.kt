package com.evgenii.photosearch.photolistscreen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.evgenii.photosearch.core.presentation.model.Event
import com.evgenii.photosearch.photolistscreen.domain.usecases.GetPhotoListUseCase
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
    private val getPhotoListUseCase: GetPhotoListUseCase
) : ViewModel() {

    private var _photoListFlow: Flow<PagingData<PhotoItem>>? = null
    val photoListFlow: Flow<PagingData<PhotoItem>>?
        get() = _photoListFlow

    private var _photoListUpdate = MutableLiveData<Unit>()
    val photoListUpdate: LiveData<Unit> = _photoListUpdate

    private val _screenState: MutableLiveData<PhotoListScreenState> = MutableLiveData()
    val screenState: LiveData<PhotoListScreenState> = _screenState

    private val _commands: MutableLiveData<Event<Commands>> = MutableLiveData()
    val commands: LiveData<Event<Commands>> = _commands

    init {
        _screenState.value = PhotoListScreenState()
    }

    fun searchPhotos(query: String) {
        _photoListFlow = getPhotoListUseCase(query)
            .cachedIn(viewModelScope).map { pagingData ->
                pagingData.map { photo ->
                    mapper.mapPhotoToPhotoItem(photo)
                }
            }
        _photoListUpdate.value = Unit
        _commands.value = Event(HideKeyboard)
        _screenState.value = PhotoListScreenState(
            photoListVisibility = true,
            loadingProgressBarVisibility = true
        )
    }

    fun onLoadStateListener(loadState: CombinedLoadStates, itemCount: Int) {
        val refreshState = loadState.refresh
        if (refreshState.isLoadError()) {
            _screenState.value = PhotoListScreenState(
                errorTextViewVisibility = true,
                errorType = ErrorType.NETWORK
            )
            Timber.d((refreshState as LoadState.Error).error.localizedMessage)
        } else if (refreshState.isNotLoadingProcess()) {
            if (loadState.isLoadEmpty(itemCount)) {
                _screenState.value = PhotoListScreenState(
                    errorTextViewVisibility = true,
                    errorType = ErrorType.NOT_FOUND
                )
            } else if (itemCount > 0) {
                _screenState.value = PhotoListScreenState(
                    photoListVisibility = true,
                )
            }
        }
    }

    fun onPhotoDetails(photoItem: PhotoItem) {
        _commands.value = Event(ShowDetail(photoItem))
    }

    fun onRetryClick(query: String) =
        searchPhotos(query)

    private fun LoadState.isLoadError(): Boolean =
        this is LoadState.Error

    private fun CombinedLoadStates.isLoadEmpty(itemCount: Int): Boolean =
        source.refresh is LoadState.NotLoading &&
                append.endOfPaginationReached && itemCount == 0

    private fun LoadState.isNotLoadingProcess(): Boolean =
        this !is LoadState.Loading
}
