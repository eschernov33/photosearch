package com.evgenii.photosearch.detailscreen.domain.usecases

import com.evgenii.photosearch.detailscreen.domain.model.PhotoComment
import com.evgenii.photosearch.detailscreen.domain.repository.PhotoCommentRepository
import javax.inject.Inject

class PhotoCommentInsertUseCase @Inject constructor(
    private val repository: PhotoCommentRepository
) {

    suspend fun insertPhotoComment(photoComment: PhotoComment) =
        repository.insertPhotoComment(photoComment)
}