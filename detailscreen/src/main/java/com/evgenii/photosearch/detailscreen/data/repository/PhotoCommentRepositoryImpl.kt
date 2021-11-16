package com.evgenii.photosearch.detailscreen.data.repository

import com.evgenii.photosearch.detailscreen.data.database.PhotoCommentDao
import com.evgenii.photosearch.detailscreen.domain.model.PhotoComment
import com.evgenii.photosearch.detailscreen.domain.repository.PhotoCommentRepository

class PhotoCommentRepositoryImpl(private val dao: PhotoCommentDao) : PhotoCommentRepository {

    override suspend fun getPhotoCommentById(id: Int): PhotoComment? =
        dao.getPhotoCommentById(id)

    override suspend fun insertPhotoComment(photoComment: PhotoComment) =
        dao.insertPhotoComment(photoComment)
}