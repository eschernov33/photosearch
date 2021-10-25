package com.evgenii.photosearch.detailscreen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evgenii.photosearch.core.presentation.model.Event
import com.evgenii.photosearch.core.presentation.model.PhotoDetailItem
import com.evgenii.photosearch.detailscreen.domain.usecases.GetPhotoByIdUseCase
import com.evgenii.photosearch.detailscreen.presentation.mapper.PhotoItemMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PhotoDetailViewModel @Inject constructor(
    private val getPhotoByIdUseCase: GetPhotoByIdUseCase,
    private val mapper: PhotoItemMapper
) : ViewModel() {

    private var _photoDetail: MutableLiveData<PhotoDetailItem> = MutableLiveData()
    val photoDetail: LiveData<PhotoDetailItem> = _photoDetail

    private var _isPhotoLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isPhotoLoading: LiveData<Boolean> = _isPhotoLoading

    private val _eventShowToastError: MutableLiveData<Event<Unit>> = MutableLiveData()
    val eventShowToastError: LiveData<Event<Unit>> = _eventShowToastError

    private val _eventToBackScreen: MutableLiveData<Event<Unit>> = MutableLiveData()
    val eventToBackScreen: LiveData<Event<Unit>> = _eventToBackScreen

    private val _eventOpenInBrowser: MutableLiveData<Event<String>> = MutableLiveData()
    val eventOpenInBrowser: LiveData<Event<String>> = _eventOpenInBrowser

    fun loadDetailInfo(photoId: Int) {
        if (photoDetail.value != null)
            return

        _isPhotoLoading.value = true
        viewModelScope.launch {
            val photoDetail = getPhotoByIdUseCase(photoId)
            if (photoDetail == null) {
                _eventShowToastError.value = Event(Unit)
                _eventToBackScreen.value = Event(Unit)
            } else {
                _isPhotoLoading.value = false
                _photoDetail.value = mapper.mapPhotoToPhotoDetailItem(photoDetail)
            }
        }
    }

    fun onOpenInBrowserClick() =
        _photoDetail.value?.let { photoDetailItem ->
            _eventOpenInBrowser.value = Event(photoDetailItem.pageURL)
        }

    fun onBackButtonPressed() {
        _eventToBackScreen.value = Event(Unit)
    }
}