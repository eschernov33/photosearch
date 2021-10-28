package com.evgenii.photosearch.detailscreen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evgenii.photosearch.core.presentation.model.Event
import com.evgenii.photosearch.detailscreen.domain.usecases.PhotoByIdUseCase
import com.evgenii.photosearch.detailscreen.presentation.mapper.PhotoItemMapper
import com.evgenii.photosearch.detailscreen.presentation.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class PhotoDetailViewModel @Inject constructor(
    private val photoByIdUseCase: PhotoByIdUseCase,
    private val mapper: PhotoItemMapper
) : ViewModel() {

    private var _photoDetailScreenState: MutableLiveData<PhotoDetailScreenState> = MutableLiveData()
    val photoDetailScreenState: LiveData<PhotoDetailScreenState> = _photoDetailScreenState

    private val _commands: MutableLiveData<Event<Commands>> = MutableLiveData()
    val commands: LiveData<Event<Commands>> = _commands

    fun onInitScreen(photoId: Int) {
        val screenState = photoDetailScreenState.value
        if (screenState == null || !screenState.isLoading) {
            loadPhotoDetailInfo(photoId)
        }
    }

    fun onOpenInBrowserClick() =
        _photoDetailScreenState.value?.let { photoDetailItem ->
            _commands.value = Event(OpenInBrowser(photoDetailItem.photoPageUrl))
        }

    fun onBackButtonPressed() {
        _commands.value = Event(NavigateToBackScreen)
    }

    private fun loadPhotoDetailInfo(photoId: Int) {
        _photoDetailScreenState.value = PhotoDetailScreenState(progressBarVisibility = true)
        viewModelScope.launch {
            val photoDetail = photoByIdUseCase.getPhoto(photoId)
            if (photoDetail == null) {
                _commands.value = Event(ShowToast)
                _commands.value = Event(NavigateToBackScreen)
            } else {
                val photoDetailItem = mapper.mapPhotoToPhotoDetailItem(photoDetail)
                _photoDetailScreenState.value =
                    PhotoDetailScreenState(
                        progressBarVisibility = false,
                        userName = photoDetailItem.user,
                        likeCount = photoDetailItem.likes,
                        downloadCount = photoDetailItem.downloads,
                        tags = photoDetailItem.tags,
                        commentCount = photoDetailItem.comments,
                        viewsCount = photoDetailItem.views,
                        userImageURL = photoDetailItem.userImageURL,
                        photoPageUrl = photoDetailItem.pageURL
                    )
            }
        }
    }
}