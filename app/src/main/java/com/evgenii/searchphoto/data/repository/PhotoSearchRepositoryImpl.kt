package com.evgenii.searchphoto.data.repository

import com.evgenii.searchphoto.data.mapper.HitApiMapper
import com.evgenii.searchphoto.data.model.HitApiList
import com.evgenii.searchphoto.data.service.Constants
import com.evgenii.searchphoto.data.service.PhotosService
import com.evgenii.searchphoto.domain.model.Photo
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PhotoSearchRepositoryImpl : PhotoSearchRepository {

    private val mapper = HitApiMapper()

    private val photosService: PhotosService by lazy {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.API_PIXABAY_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        retrofit.create(PhotosService::class.java)
    }

    override fun getPhotos(
        query: String,
        page: Int,
        onResponse: (List<Photo>) -> Unit,
        onFailure: (t: Throwable) -> Unit
    ) {
        val responseCall = photosService.getPhotos(query, page)
        responseCall.enqueue(object : Callback<HitApiList> {
            override fun onResponse(call: Call<HitApiList>, response: Response<HitApiList>) {
                val responsePhotoList = response.body()
                if (responsePhotoList == null) {
                    onResponse(emptyList())
                } else {
                    val photoList = mapper.mapHitApiListToEntity(responsePhotoList)
                    onResponse(photoList)
                }
            }

            override fun onFailure(call: Call<HitApiList>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}