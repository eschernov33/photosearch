package com.evgenii.photosearch.detailscreen.data.repository

import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.mapper.HitMapper
import com.evgenii.photosearch.core.data.model.HitResponse
import com.evgenii.photosearch.core.domain.model.PhotoDetail
import com.evgenii.photosearch.detailscreen.domain.repository.PhotoSearchDetailRepository
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class PhotoSearchDetailRepositoryImpl @Inject constructor(
    private val api: PhotosApi,
    private val mapper: HitMapper,
) : PhotoSearchDetailRepository {

    override suspend fun getPhotoById(photoId: Int): List<PhotoDetail>? =
        try {
            val response: Response<HitResponse> = api.getPhotoById(photoId)
            val hitApiResponse: HitResponse? = response.body()
            hitApiResponse?.let(mapper::mapHitResponseToListPhotoDetail)
        } catch (exception: Exception) {
            Timber.e(exception)
            null
        }
}