package com.evgenii.photosearch.detailscreen.domain.repository

import com.evgenii.core.domain.model.PhotoDetail
import io.reactivex.Single

interface PhotoSearchByIdRepository {

    fun getPhotoById(photoId: Int): Single<List<PhotoDetail>>
}