package com.evgenii.searchphoto.data.api

import com.evgenii.core.data.model.HitApiResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosApi {

    @GET("?image_type=photo")
    fun getPhotos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("key") key: String = API_KEY
    ): Single<HitApiResponse>

    @GET("?image_type=photo")
    fun getPhotoById(
        @Query("id") query: Int,
        @Query("key") key: String = API_KEY
    ): Single<HitApiResponse>

    companion object {
        private const val API_KEY = "23542045-e66ff76ab11368fd9f4fd2afe"
    }
}