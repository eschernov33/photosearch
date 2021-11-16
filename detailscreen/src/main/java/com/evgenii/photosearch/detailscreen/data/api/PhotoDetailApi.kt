package com.evgenii.photosearch.detailscreen.data.api

import com.evgenii.photosearch.core.BuildConfig
import com.evgenii.photosearch.core.data.model.PhotoApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoDetailApi {

    @GET("?image_type=photo")
    suspend fun getPhotoById(
        @Query("id") query: Int,
        @Query("key") key: String = BuildConfig.PixabayApiKey
    ): Response<PhotoApiResponse>
}