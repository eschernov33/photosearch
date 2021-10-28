package com.evgenii.photosearch.detailscreen.data.api

import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.api.PhotosApi.Companion.API_KEY
import com.evgenii.photosearch.core.data.model.PhotoApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoDetailApi : PhotosApi {

    @GET("?image_type=photo")
    suspend fun getPhotoById(
        @Query("id") query: Int,
        @Query("key") key: String = API_KEY
    ): Response<PhotoApiResponse>
}