package com.evgenii.searchphoto.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.paging.*
import com.evgenii.searchphoto.domain.usecases.GetPhotoListUseCase
import com.evgenii.searchphoto.presentation.mapper.PhotoItemMapper
import com.evgenii.searchphoto.presentation.model.*
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

    private val _photosLoadState = MutableLiveData<PhotosLoadState>()
    val photosLoadState: LiveData<PhotosLoadState> = _photosLoadState

    private val _actionShowDetails: MutableLiveData<Event<PhotoItem>> = MutableLiveData()
    val actionShowDetails: LiveData<Event<PhotoItem>> = _actionShowDetails

    init {
        _photosLoadState.value = OnInitState
    }

    fun searchPhotos(query: String) {
        _photosLoadState.value = StartLoadingState
        _photoList = getPhotoListUseCase(query)
            .map { pagingData ->
                pagingData.map { mapper.mapPhotoToPhotoItem(it) }
            }.cachedIn(this) as MutableLiveData<PagingData<PhotoItem>>
    }

    fun onLoadStateListener(loadState: CombinedLoadStates, itemCount: Int) {
        val refreshState = loadState.refresh
        if (refreshState is LoadState.Error) {
            _photosLoadState.value = EndLoadingState
            _photosLoadState.value = ErrorLoadState(
                (refreshState.error.localizedMessage)
            )
        } else if (loadState.refresh !is LoadState.Loading) {
            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached && itemCount < 1
            ) {
                _photosLoadState.value = EndLoadingState
                _photosLoadState.value = EmptyLoadState
            } else if (itemCount > 0) {
                _photoList.value?.let { pagingData ->
                    _photosLoadState.value = EndLoadingState
                    _photosLoadState.value = SuccessLoadState(pagingData)
                }
            }
        }
    }

    fun onPhotoDetails(photoItem: PhotoItem) {
        _actionShowDetails.value = Event(photoItem)
    }
}
