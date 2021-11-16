package com.evgenii.photosearch.detailscreen.domain.repository

import com.evgenii.photosearch.detailscreen.domain.model.PhotoDetail

interface PhotoSearchDetailRepository {

    suspend fun getPhotoById(photoId: Int, debugMode: Boolean = false): PhotoDetail?
}