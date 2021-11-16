package com.evgenii.photosearch.detailscreen.domain.repository

import com.evgenii.photosearch.detailscreen.domain.model.PhotoComment

interface PhotoCommentRepository {

    suspend fun getPhotoCommentById(id: Int): PhotoComment?

    suspend fun insertPhotoComment(photoComment: PhotoComment)
}