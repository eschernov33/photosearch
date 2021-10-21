package com.evgenii.photosearch.detailscreen.data.repository

import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.mapper.HitApiMapper
import com.evgenii.photosearch.core.domain.model.PhotoDetail
import com.evgenii.photosearch.detailscreen.domain.repository.PhotoSearchByIdRepository
import io.reactivex.Single
import javax.inject.Inject

class PhotoSearchByIdRepositoryImpl @Inject constructor(
    private val api: PhotosApi,
    private val mapper: HitApiMapper,
) : PhotoSearchByIdRepository {

    override fun getPhotoById(photoId: Int): Single<List<PhotoDetail>> =
        api.getPhotoById(photoId).map(mapper::mapHitApiResponseToListPhotoDetail)

}