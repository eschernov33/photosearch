package com.evgenii.photosearch.detailscreen.data.api

import com.evgenii.photosearch.core.data.api.PhotosApi.Companion.API_PIXABAY_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class PhotoDetailApiBuilder @Inject constructor() {

    fun buildApi(): PhotoDetailApi {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(API_PIXABAY_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(PhotoDetailApi::class.java)
    }
}