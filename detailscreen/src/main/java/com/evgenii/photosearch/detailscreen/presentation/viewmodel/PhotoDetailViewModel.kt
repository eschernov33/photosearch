package com.evgenii.photosearch.detailscreen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evgenii.photosearch.core.presentation.model.Event
import com.evgenii.photosearch.core.presentation.model.PhotoDetailItem
import com.evgenii.photosearch.detailscreen.domain.usecases.GetPhotoByIdUseCase
import com.evgenii.photosearch.detailscreen.presentation.mapper.PhotoItemMapper
import com.evgenii.photosearch.detailscreen.presentation.model.*
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

    private var _photoDetailScreenState: MutableLiveData<PhotoDetailScreenState> = MutableLiveData()
    val photoDetailScreenState: LiveData<PhotoDetailScreenState> = _photoDetailScreenState

    private val _commands: MutableLiveData<Event<Commands>> = MutableLiveData()
    val commands: LiveData<Event<Commands>> = _commands

    fun loadDetailInfo(photoId: Int) {
        if (photoDetail.value != null) {
            return
        }
        _photoDetailScreenState.value = PhotoDetailScreenState(progressBarVisibility = true)
        viewModelScope.launch {
            val photoDetail = getPhotoByIdUseCase(photoId)
            if (photoDetail == null) {
                _commands.value = Event(ShowToast)
                _commands.value = Event(NavigateToBackScreen)
            } else {
                _photoDetailScreenState.value =
                    PhotoDetailScreenState(progressBarVisibility = false)
                _photoDetail.value = mapper.mapPhotoToPhotoDetailItem(photoDetail)
            }
        }
    }

    fun onOpenInBrowserClick() =
        _photoDetail.value?.let { photoDetailItem ->
            _commands.value = Event(OpenInBrowser(photoDetailItem.pageURL))
        }

    fun onBackButtonPressed() {
        _commands.value = Event(NavigateToBackScreen)
    }
}