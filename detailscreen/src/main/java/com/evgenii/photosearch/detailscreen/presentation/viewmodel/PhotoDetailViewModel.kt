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

    private val _showToastError: MutableLiveData<Event<Unit>> = MutableLiveData()
    val showToastError: LiveData<Event<Unit>> = _showToastError

    private val _navigateToBackScreen: MutableLiveData<Event<Unit>> = MutableLiveData()
    val navigateToBackScreen: LiveData<Event<Unit>> = _navigateToBackScreen

    private val _openInBrowser: MutableLiveData<Event<String>> = MutableLiveData()
    val openInBrowser: LiveData<Event<String>> = _openInBrowser

    fun loadDetailInfo(photoId: Int) {
        if (photoDetail.value != null)
            return

        _isPhotoLoading.value = true
        viewModelScope.launch {
            val photoDetail = getPhotoByIdUseCase(photoId)
            if (photoDetail == null) {
                _showToastError.value = Event(Unit)
                _navigateToBackScreen.value = Event(Unit)
            } else {
                _isPhotoLoading.value = false
                _photoDetail.value = mapper.mapPhotoToPhotoDetailItem(photoDetail)
            }
        }
    }

    fun onOpenInBrowserClick() =
        _photoDetail.value?.let { photoDetailItem ->
            _openInBrowser.value = Event(photoDetailItem.pageURL)
        }

    fun onBackButtonPressed() {
        _navigateToBackScreen.value = Event(Unit)
    }
}