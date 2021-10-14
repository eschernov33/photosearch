package com.evgenii.searchphoto

import android.app.Application
import com.evgenii.searchphoto.data.api.Constants
import com.evgenii.searchphoto.data.api.PhotosApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.hilt.android.HiltAndroidApp
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

@HiltAndroidApp
class App : Application() {

    val photosApi: PhotosApi by lazy {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.API_PIXABAY_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        retrofit.create(PhotosApi::class.java)
    }
}