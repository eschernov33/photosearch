package com.evgenii.photosearch.detailscreen.data.repository

import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.mapper.PhotoApiMapper
import com.evgenii.photosearch.core.data.model.PhotoApiResponse
import com.evgenii.photosearch.core.domain.model.PhotoDetail
import com.evgenii.photosearch.detailscreen.domain.repository.PhotoSearchDetailRepository
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class PhotoSearchDetailRepositoryImpl @Inject constructor(
    private val api: PhotosApi,
    private val mapper: PhotoApiMapper,
) : PhotoSearchDetailRepository {

    override suspend fun getPhotoById(photoId: Int): PhotoDetail? =
        try {
            val response: Response<PhotoApiResponse> = api.getPhotoById(photoId)
            val photoApiApiResponse: PhotoApiResponse? = response.body()
            photoApiApiResponse?.let(mapper::mapPhotoApiResponseToListPhotoDetail)?.firstOrNull()
        } catch (exception: Exception) {
            Timber.e(exception)
            null
        }
}