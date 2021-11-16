package com.evgenii.photosearch.detailscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evgenii.photosearch.detailscreen.domain.model.PhotoComment
import com.evgenii.photosearch.detailscreen.domain.usecases.PhotoByIdUseCase
import com.evgenii.photosearch.detailscreen.domain.usecases.PhotoCommentByIdUseCase
import com.evgenii.photosearch.detailscreen.domain.usecases.PhotoCommentInsertUseCase
import com.evgenii.photosearch.detailscreen.presentation.mapper.PhotoDetailItemMapper
import com.evgenii.photosearch.detailscreen.presentation.model.DetailLoadState
import com.evgenii.photosearch.detailscreen.presentation.model.DetailScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val photoByIdUseCase: PhotoByIdUseCase,
    private val photoCommentByIdUseCase: PhotoCommentByIdUseCase,
    private val photoCommentInsertUseCase: PhotoCommentInsertUseCase,
    private val mapperDetail: PhotoDetailItemMapper
) : ViewModel() {

    private val _screenState = MutableStateFlow(DetailScreenState())
    val screenState: StateFlow<DetailScreenState> = _screenState

    private var photoId: Int? = null

    private fun loadPhotoDetailInfo(photoId: Int) {
        _screenState.value = _screenState.value.copy(loadState = DetailLoadState.Loading)
        viewModelScope.launch {
            val photoDetail = photoByIdUseCase.getPhoto(photoId)
            if (photoDetail == null) {
                _screenState.value = _screenState.value.copy(loadState = DetailLoadState.LoadError)
            } else {
                val photoDetailItem = mapperDetail.mapPhotoToPhotoDetailItem(photoDetail)
                val comment: PhotoComment? =
                    photoCommentByIdUseCase.getPhotoCommentById(photoDetailItem.id)
                _screenState.value = _screenState.value.copy(
                    photoDetail = photoDetailItem,
                    loadState = DetailLoadState.LoadSuccess,
                    comment = comment?.comment ?: ""
                )
            }
        }
    }

    fun onInitScreen(photoId: Int) {
        if (this.photoId == null) {
            this.photoId = photoId
            loadPhotoDetailInfo(photoId)
        }
    }

    private fun saveComment(photoComment: PhotoComment) =
        viewModelScope.launch {
            photoCommentInsertUseCase.insertPhotoComment(photoComment)
        }

    fun onSaveCommentClick(comment: String) =
        photoId?.let { id ->
            saveComment(PhotoComment(id, comment))
        }

    fun onRetryClick() =
        photoId?.let(this::loadPhotoDetailInfo)
}