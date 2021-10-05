package com.evgenii.searchphoto

import android.app.Application
import com.evgenii.searchphoto.data.api.Constants
import com.evgenii.searchphoto.data.api.PhotosApi
import com.evgenii.searchphoto.data.mapper.HitApiMapper
import com.evgenii.searchphoto.data.repository.PhotoSearchRepositoryImpl
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class App : Application() {

    private val photosApi: PhotosApi by lazy {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.API_PIXABAY_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        retrofit.create(PhotosApi::class.java)
    }

    val photoSearchRepository: PhotoSearchRepository = PhotoSearchRepositoryImpl(
        HitApiMapper(),
        photosApi
    )
}