package com.evgenii.photosearch.detailscreen.data.repository

import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.mapper.HitApiMapper
import com.evgenii.photosearch.core.domain.model.PhotoDetail
import com.evgenii.photosearch.detailscreen.domain.repository.PhotoSearchDetailRepository
import timber.log.Timber
import javax.inject.Inject

class PhotoSearchDetailRepositoryImpl @Inject constructor(
    private val api: PhotosApi,
    private val mapper: HitApiMapper,
) : PhotoSearchDetailRepository {

    override suspend fun getPhotoById(photoId: Int): List<PhotoDetail>? =
        try {
            val response = api.getPhotoById(photoId)
            val hitApiResponse = response.body()
            hitApiResponse?.let(mapper::mapHitApiResponseToListPhotoDetail)
        } catch (exception: Exception) {
            Timber.e(exception)
            null
        }
}