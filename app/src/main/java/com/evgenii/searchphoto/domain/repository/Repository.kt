package com.evgenii.searchphoto.domain.repository

import com.evgenii.searchphoto.data.mapper.ApiMapper
import com.evgenii.searchphoto.data.model.HitsResponseApi
import com.evgenii.searchphoto.data.service.PhotosService
import com.evgenii.searchphoto.domain.model.PhotoItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class Repository(
    retrofit: Retrofit,
    private val onPhotosLoad: (photosList: List<PhotoItem>)->Unit
) {

    private val photosService: PhotosService = retrofit.create(PhotosService::class.java)
    private val mapper = ApiMapper
    fun getPhotosCall(query: String, page: Int = 1): Call<HitsResponseApi> =
        photosService.getPhotos(query, page)

    fun getPhotosList(query: String, page: Int = 1){
        val callHitsResponseApi = photosService.getPhotos(query, page)
        callHitsResponseApi.enqueue(object : Callback<HitsResponseApi> {
            override fun onResponse(
                call: Call<HitsResponseApi>,
                response: Response<HitsResponseApi>,
            ) {
                val listHits = response.body()
                if (listHits != null) {
                    onPhotosLoad(mapper.mapFromHitList(listHits.hits))
                } else {
                    onPhotosLoad(mapper.mapFromHitList(emptyList()))
                }
            }

            override fun onFailure(call: Call<HitsResponseApi>, t: Throwable) {
                onPhotosLoad(mapper.mapFromHitList(emptyList()))
            }
        })
    }

}