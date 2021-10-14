package com.evgenii.searchphoto.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import com.evgenii.searchphoto.data.api.PhotosApi
import com.evgenii.searchphoto.data.source.PhotoListRxPageSource
import com.evgenii.searchphoto.domain.usecases.LoadPhotoListUseCase
import com.evgenii.searchphoto.presentation.mapper.PhotoItemMapper
import com.evgenii.searchphoto.presentation.model.PhotoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val photosApi: PhotosApi,
    private val loadPhotoListUseCase: LoadPhotoListUseCase,
    private val mapper: PhotoItemMapper
) : ViewModel() {

    private var _photoList: LiveData<PagingData<PhotoItem>> = MutableLiveData()
    val photoList: LiveData<PagingData<PhotoItem>>
        get() = _photoList

    private val _photoListVisibility = MutableLiveData<Boolean>()
    val photoListVisibility: LiveData<Boolean> = _photoListVisibility

    private val _textSearchResultError: MutableLiveData<String> = MutableLiveData()
    val textSearchResultError: LiveData<String> = _textSearchResultError

    private val _textSearchResultErrorVisible: MutableLiveData<Boolean> = MutableLiveData()
    val textSearchResultErrorVisible: LiveData<Boolean> = _textSearchResultErrorVisible

    private val _showProgressBar: MutableLiveData<Boolean> = MutableLiveData()
    val showProgressBar: LiveData<Boolean> = _showProgressBar

    private val _shouldHideSoftKeyboard = MutableLiveData<Unit>()
    val shouldHideSoftKeyboard: LiveData<Unit> = _shouldHideSoftKeyboard

    private val _shouldShowToast = MutableLiveData<String>()
    val shouldShowToast: LiveData<String> = _shouldShowToast

    init {
        _showProgressBar.value = false
        _photoListVisibility.value = false
        _textSearchResultErrorVisible.value = false
    }

    fun searchPhotos(query: String) {
        _showProgressBar.value = true
        _photoList = loadPhotoListUseCase.execute(
            photosApi,
            query,
            1,
            PhotoListRxPageSource(photosApi, query)
        )
            .map { pagingData ->
                pagingData.map { mapper.mapPhotoToPhotoItem(it) }
            }
        _showProgressBar.value = false
    }

    fun onLoadStateListener(loadState: CombinedLoadStates, itemCount: Int) {
        val refreshState = loadState.refresh
        if (refreshState is LoadState.Error) {
            _textSearchResultError.value = "Проверьте ваше интернет соединение. Ошибка: " +
                    (refreshState.error.localizedMessage ?: "неизвестная ошибка")
            _textSearchResultErrorVisible.value = true
            _photoListVisibility.value = false
        } else if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && itemCount < 1) {
            _textSearchResultError.value = "Фото не найдены"
            _textSearchResultErrorVisible.value = true
            _photoListVisibility.value = false
        } else if (itemCount > 0) {
            _shouldHideSoftKeyboard.value = Unit
            _textSearchResultErrorVisible.value = false
            _photoListVisibility.value = true
        }
    }

    fun onItemClick(photoItem: PhotoItem) {
        _shouldShowToast.value = photoItem.user
    }
}