package com.evgenii.photosearch.core.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

class RemoteDataSource @Inject constructor() {

    fun buildApi(): PhotosApi {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(API_PIXABAY_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(PhotosApi::class.java)
    }

    companion object {
        private const val API_PIXABAY_URL = "https://pixabay.com/api/"
    }
}