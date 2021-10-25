package com.evgenii.photosearch.photolistscreen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.evgenii.photosearch.core.presentation.model.Event
import com.evgenii.photosearch.photolistscreen.domain.usecases.GetPhotoListUseCase
import com.evgenii.photosearch.photolistscreen.presentation.mapper.PhotoItemMapper
import com.evgenii.photosearch.photolistscreen.presentation.model.ErrorType
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoItem
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoListViewsVisibility
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

    private var _photoListViewsVisibility = MutableLiveData<PhotoListViewsVisibility>()
    val photoListViewsVisibility: LiveData<PhotoListViewsVisibility> =
        _photoListViewsVisibility

    private var _errorType = MutableLiveData<ErrorType>()
    val errorMessage: LiveData<ErrorType> = _errorType

    private val _eventShowDetails: MutableLiveData<Event<PhotoItem>> = MutableLiveData()
    val eventShowDetails: LiveData<Event<PhotoItem>> = _eventShowDetails

    private val _eventHideKeyboard: MutableLiveData<Event<Unit>> = MutableLiveData()
    val eventHideKeyboard: LiveData<Event<Unit>> = _eventHideKeyboard

    init {
        updatePhotoListAreaViewVisibility()
    }

    fun searchPhotos(query: String) {
        _photoListFlow = getPhotoListUseCase(query)
            .cachedIn(viewModelScope).map { pagingData ->
                pagingData.map { photo ->
                    mapper.mapPhotoToPhotoItem(photo)
                }
            }
        _photoListUpdate.value = Unit
        _eventHideKeyboard.value = Event(Unit)
        updatePhotoListAreaViewVisibility(listVisible = true, progressPhotoLoadVisible = true)
    }

    fun onLoadStateListener(loadState: CombinedLoadStates, itemCount: Int) {
        val refreshState = loadState.refresh
        if (refreshState.isLoadError()) {
            _errorType.value = ErrorType.NETWORK
            updatePhotoListAreaViewVisibility(errorMessageVisible = true, btnRetryVisible = true)
            Timber.d((refreshState as LoadState.Error).error.localizedMessage)
        } else if (refreshState.isNotLoading()) {
            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached && itemCount == 0
            ) {
                updatePhotoListAreaViewVisibility(errorMessageVisible = true)
                _errorType.value = ErrorType.NOT_FOUND
            } else if (itemCount > 0) {
                updatePhotoListAreaViewVisibility(listVisible = true)
            }
        }
    }

    fun onPhotoDetails(photoItem: PhotoItem) {
        _eventShowDetails.value = Event(photoItem)
    }

    fun onRetryClick(query: String) =
        searchPhotos(query)

    private fun LoadState.isLoadError(): Boolean =
        this is LoadState.Error

    private fun LoadState.isNotLoading(): Boolean =
        this !is LoadState.Loading

    private fun updatePhotoListAreaViewVisibility(
        listVisible: Boolean = false,
        progressPhotoLoadVisible: Boolean = false,
        errorMessageVisible: Boolean = false,
        btnRetryVisible: Boolean = false
    ) {
        _photoListViewsVisibility.value = PhotoListViewsVisibility(
            listVisible = listVisible,
            progressPhotoLoadVisible = progressPhotoLoadVisible,
            errorMessageVisible = errorMessageVisible,
            btnRetryVisible = btnRetryVisible
        )
    }
}
