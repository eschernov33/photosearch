package com.evgenii.searchphoto

import android.app.Application
import com.evgenii.searchphoto.data.repository.PhotoSearchRepositoryImpl
import com.evgenii.searchphoto.data.service.PhotosService
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class App : Application() {

    val photoSearchRepository: PhotoSearchRepository by lazy(LazyThreadSafetyMode.NONE) {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(API_PIXABAY_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        val service = retrofit.create(PhotosService::class.java)
        PhotoSearchRepositoryImpl(service)
    }

    companion object {
        const val API_PIXABAY_URL = "https://pixabay.com/api/"
    }
}