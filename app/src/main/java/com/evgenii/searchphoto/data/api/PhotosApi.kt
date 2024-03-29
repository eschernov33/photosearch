package com.evgenii.searchphoto.data.api

import com.evgenii.searchphoto.data.model.HitApiItemList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosApi {

    @GET("?image_type=photo")
    fun getPhotos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("key") key: String = Constants.API_KEY
    ): Call<HitApiItemList>
}