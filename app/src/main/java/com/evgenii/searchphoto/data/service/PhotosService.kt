package com.evgenii.searchphoto.data.service

import com.evgenii.searchphoto.data.model.HitsResponseApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosService {

    @GET("?image_type=photo&key=${Constants.API_KEY}")
    fun getPhotos(
        @Query("q") query: String,
        @Query("page") page: Int,
    ): Call<HitsResponseApi>
}