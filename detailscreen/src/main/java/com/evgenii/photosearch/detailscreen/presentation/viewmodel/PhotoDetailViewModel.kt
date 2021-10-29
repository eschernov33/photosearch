package com.evgenii.photosearch.detailscreen.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.evgenii.photosearch.core.presentation.viewmodel.BaseViewModel
import com.evgenii.photosearch.detailscreen.domain.usecases.PhotoByIdUseCase
import com.evgenii.photosearch.detailscreen.presentation.mapper.PhotoDetailItemMapper
import com.evgenii.photosearch.detailscreen.presentation.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val photoByIdUseCase: PhotoByIdUseCase,
    private val mapperDetail: PhotoDetailItemMapper
) : BaseViewModel<PhotoDetailScreenState, Commands>() {

    fun onInitScreen(photoId: Int) {
        val isDetailAlreadyLoading = screenState.value?.isLoading ?: false
        if (!isDetailAlreadyLoading) {
            loadPhotoDetailInfo(photoId)
        }
    }

    fun onOpenInBrowserClick() {
        screenState.value?.let { photoDetailItem ->
            executeCommand(OpenInBrowser(photoDetailItem.photoPageUrl))
        }
    }

    fun onBackButtonPressed() {
        executeCommand(NavigateToPrevScreen)
    }

    private fun loadPhotoDetailInfo(photoId: Int) {
        updateScreen(PhotoDetailScreenState(progressBarVisibility = true))
        viewModelScope.launch {
            val photoDetail = photoByIdUseCase.getPhoto(photoId)
            if (photoDetail == null) {
                executeCommand(ShowToast)
                executeCommand(NavigateToPrevScreen)
            } else {
                val photoDetailItem = mapperDetail.mapPhotoToPhotoDetailItem(photoDetail)
                updateScreen(
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
                )
            }
        }
    }
}