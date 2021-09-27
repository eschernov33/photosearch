package com.evgenii.searchphoto.data.service

import com.evgenii.searchphoto.data.model.HitsResponseApi
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosService {

    @GET("?image_type=photo&key=$API_KEY")
    fun getPhotos(
        @Query(VALUE_QUERY) query: String,
        @Query(VALUE_PAGE) page: Int = FIRST_PAGE,
    ): Call<HitsResponseApi>

    companion object {
        const val API_KEY = "23542045-e66ff76ab11368fd9f4fd2afe"
        const val FIRST_PAGE = 1
        const val VALUE_QUERY = "q"
        const val VALUE_PAGE = "page"
    }
}