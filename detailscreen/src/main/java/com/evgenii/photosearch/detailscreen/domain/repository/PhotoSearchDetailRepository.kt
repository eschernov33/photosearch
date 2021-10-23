package com.evgenii.photosearch.detailscreen.domain.repository

import com.evgenii.photosearch.core.domain.model.PhotoDetail
import io.reactivex.Single

interface PhotoSearchDetailRepository {

    fun getPhotoById(photoId: Int): Single<List<PhotoDetail>>
}