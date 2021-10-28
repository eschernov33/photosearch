package com.evgenii.photosearch.photolistscreen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.evgenii.photosearch.core.presentation.viewmodel.BaseViewModel
import com.evgenii.photosearch.photolistscreen.R
import com.evgenii.photosearch.photolistscreen.domain.usecases.PhotoListUseCase
import com.evgenii.photosearch.photolistscreen.presentation.extensions.isLoadEmpty
import com.evgenii.photosearch.photolistscreen.presentation.extensions.isLoadError
import com.evgenii.photosearch.photolistscreen.presentation.extensions.isNotLoadingProcess
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
) : BaseViewModel<PhotoListScreenState, Commands>() {

    private var userQuery: String = ""

    private var _photoListFlow: Flow<PagingData<PhotoItem>>? = null
    val photoListFlow: Flow<PagingData<PhotoItem>>?
        get() = _photoListFlow

    private var _photoListUpdated = MutableLiveData<Unit>()
    val photoListUpdated: LiveData<Unit> = _photoListUpdated

    fun onSearchTextChanged(query: String) {
        userQuery = query
    }

    fun onSearchButtonClick() =
        searchPhotos()

    fun onPhotoItemClick(photoItem: PhotoItem) {
        executeCommand(ShowDetail(photoItem))
    }

    fun onRetryButtonClick() =
        searchPhotos()

    fun onLoadStateListener(loadState: CombinedLoadStates, itemCount: Int) {
        val refreshState = loadState.refresh
        if (refreshState.isNotLoadingProcess()) {
            when {
                refreshState.isLoadError() -> {
                    updateScreen(
                        PhotoListScreenState(
                            isErrorTextVisible = true,
                            isRetryButtonVisible = true,
                            errorTextResId = R.string.error_load_description
                        )
                    )
                    Timber.d((refreshState as LoadState.Error).error.localizedMessage)
                }
                loadState.isLoadEmpty(itemCount) -> {
                    updateScreen(
                        PhotoListScreenState(
                            isErrorTextVisible = true,
                            errorTextResId = R.string.error_empty_result
                        )
                    )
                }
                itemCount > 0 -> {
                    updateScreen(
                        PhotoListScreenState(
                            isPhotoListVisible = true,
                        )
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
        executeCommand(HideKeyboard)
        updateScreen(
            PhotoListScreenState(
                isPhotoListVisible = true,
                isLoadingProgressBarVisible = true
            )
        )
    }
}
