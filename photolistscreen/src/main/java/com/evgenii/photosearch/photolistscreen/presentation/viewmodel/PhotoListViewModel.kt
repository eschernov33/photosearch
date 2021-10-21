package com.evgenii.photosearch.photolistscreen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.paging.*
import com.evgenii.photosearch.photolistscreen.domain.usecases.GetPhotoListUseCase
import com.evgenii.photosearch.photolistscreen.presentation.mapper.PhotoItemMapper
import com.evgenii.photosearch.photolistscreen.presentation.model.ErrorMessage
import com.evgenii.photosearch.photolistscreen.presentation.model.Event
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoItem
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoListAreaViewsVisibility
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val getPhotoListUseCase: GetPhotoListUseCase,
    private val mapper: PhotoItemMapper
) : ViewModel() {

    private var _photoList = MutableLiveData<PagingData<PhotoItem>>()
    val photoList: LiveData<PagingData<PhotoItem>>
        get() = _photoList

    private var _photoListAreaViewVisibility = MutableLiveData<PhotoListAreaViewsVisibility>()
    val photoListAreaViewsVisibility: LiveData<PhotoListAreaViewsVisibility> =
        _photoListAreaViewVisibility

    private var _errorMessage = MutableLiveData<ErrorMessage>()
    val errorMessage: LiveData<ErrorMessage> = _errorMessage

    private val _actionShowDetails: MutableLiveData<Event<PhotoItem>> = MutableLiveData()
    val actionShowDetails: LiveData<Event<PhotoItem>> = _actionShowDetails

    private val _actionHideKeyboard: MutableLiveData<Event<Unit>> = MutableLiveData()
    val actionHideKeyboard: LiveData<Event<Unit>> = _actionHideKeyboard

    init {
        updatePhotoListAreaViewVisibility()
    }

    fun searchPhotos(query: String) {
        updatePhotoListAreaViewVisibility(
            listVisible = true,
            progressPhotoLoadVisible = true,
        )
        _actionHideKeyboard.value = Event(Unit)
        _photoList = getPhotoListUseCase(query)
            .map { pagingData ->
                pagingData.map { mapper.mapPhotoToPhotoItem(it) }
            }.cachedIn(this) as MutableLiveData<PagingData<PhotoItem>>
    }

    fun onLoadStateListener(loadState: CombinedLoadStates, itemCount: Int) {
        val refreshState = loadState.refresh
        if (refreshState is LoadState.Error) {
            updatePhotoListAreaViewVisibility(errorMessageVisible = true)
            _errorMessage.value =
                ErrorMessage(ErrorMessage.Type.NETWORK, refreshState.error.localizedMessage)
        } else if (loadState.refresh !is LoadState.Loading) {
            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached && itemCount < 1
            ) {
                updatePhotoListAreaViewVisibility(errorMessageVisible = true)
                _errorMessage.value = ErrorMessage(ErrorMessage.Type.NOT_FOUND)
            } else if (itemCount > 0) {
                updatePhotoListAreaViewVisibility(listVisible = true)
            }
        }
    }

    fun onPhotoDetails(photoItem: PhotoItem) {
        _actionShowDetails.value = Event(photoItem)
    }

    private fun updatePhotoListAreaViewVisibility(
        listVisible: Boolean = false,
        progressPhotoLoadVisible: Boolean = false,
        errorMessageVisible: Boolean = false
    ) {
        _photoListAreaViewVisibility.value = PhotoListAreaViewsVisibility(
            listVisible = listVisible,
            progressPhotoLoadVisible = progressPhotoLoadVisible,
            errorMessageVisible = errorMessageVisible
        )
    }
}
