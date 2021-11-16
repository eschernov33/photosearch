package com.evgenii.photosearch.photolistscreen.data.api

import com.evgenii.photosearch.core.BuildConfig
import com.evgenii.photosearch.core.data.model.PhotoApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoListApi {

    @GET("?image_type=photo")
    suspend fun getPhotos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("key") key: String = BuildConfig.PixabayApiKey,
    ): Response<PhotoApiResponse>
}