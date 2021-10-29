package com.evgenii.photosearch.detailscreen.data.repository

import com.evgenii.photosearch.core.data.model.PhotoApiResponse
import com.evgenii.photosearch.detailscreen.data.api.PhotoDetailApi
import com.evgenii.photosearch.detailscreen.data.mapper.PhotoDetailApiMapper
import com.evgenii.photosearch.detailscreen.domain.model.PhotoDetail
import com.evgenii.photosearch.detailscreen.domain.repository.PhotoSearchDetailRepository
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class PhotoSearchDetailRepositoryImpl @Inject constructor(
    private val api: PhotoDetailApi,
    private val mapper: PhotoDetailApiMapper,
) : PhotoSearchDetailRepository {

    override suspend fun getPhotoById(photoId: Int): PhotoDetail? =
        try {
            val response: Response<PhotoApiResponse> = api.getPhotoById(photoId)
            val photoApiResponse: PhotoApiResponse? = response.body()
            photoApiResponse?.let(mapper::mapPhotoApiResponseToListPhotoDetail)?.firstOrNull()
        } catch (exception: Exception) {
            Timber.e(exception)
            null
        }
}