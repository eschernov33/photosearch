package com.evgenii.photosearch.core.data.api

import com.evgenii.photosearch.core.data.api.PhotosApi.Companion.API_PIXABAY_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PhotoApiBuilder {

    companion object {
        fun buildApi(): Retrofit {
            val moshi = Moshi.Builder()
                .addLast(KotlinJsonAdapterFactory())
                .build()
            return Retrofit.Builder()
                .baseUrl(API_PIXABAY_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }
    }
}