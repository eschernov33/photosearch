package com.evgenii.photosearch.detailscreen.domain.repository

import com.evgenii.photosearch.core.domain.model.PhotoDetail

interface PhotoSearchDetailRepository {

    suspend fun getPhotoById(photoId: Int): List<PhotoDetail>?
}