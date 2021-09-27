package com.evgenii.searchphoto.data.service

import com.evgenii.searchphoto.data.model.HitsResponseApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosService {

    @GET("?image_type=photo&key=$API_KEY")
    fun getPhotos(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
    ): Call<HitsResponseApi>

    companion object {
        const val API_KEY = "23542045-e66ff76ab11368fd9f4fd2afe"
    }
}