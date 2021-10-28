package com.evgenii.photosearch.core.data.api

import com.evgenii.photosearch.core.data.model.PhotoApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosApi {

    @GET("?image_type=photo")
    suspend fun getPhotos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("key") key: String = API_KEY
    ): Response<PhotoApiResponse>

    @GET("?image_type=photo")
    suspend fun getPhotoById(
        @Query("id") query: Int,
        @Query("key") key: String = API_KEY
    ): Response<PhotoApiResponse>

    companion object {
        private const val API_KEY = "23542045-e66ff76ab11368fd9f4fd2afe"
    }
}