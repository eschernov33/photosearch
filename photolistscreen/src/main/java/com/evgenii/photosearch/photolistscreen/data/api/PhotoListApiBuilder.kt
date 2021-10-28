package com.evgenii.photosearch.photolistscreen.data.api

import com.evgenii.photosearch.core.data.api.PhotosApi.Companion.API_PIXABAY_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class PhotoListApiBuilder @Inject constructor() {

    fun buildApi(): PhotoListApi {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(API_PIXABAY_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(PhotoListApi::class.java)
    }
}