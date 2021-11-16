package com.evgenii.photosearch.detailscreen.domain.usecases

import com.evgenii.photosearch.detailscreen.domain.model.PhotoComment
import com.evgenii.photosearch.detailscreen.domain.repository.PhotoCommentRepository
import javax.inject.Inject

class PhotoCommentByIdUseCase @Inject constructor(
    private val repository: PhotoCommentRepository
) {

    suspend fun getPhotoCommentById(id: Int): PhotoComment? =
        repository.getPhotoCommentById(id)
}