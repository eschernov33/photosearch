package com.evgenii.photoofday.domain.usecases

import com.evgenii.photoofday.data.mapper.ApiMapper
import com.evgenii.photoofday.data.model.HitsResponseApi
import com.evgenii.photoofday.data.service.PhotosService
import com.evgenii.photoofday.domain.model.PhotoItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class GetPhotosUseCase(retrofit: Retrofit, private val onPhotoLoad: (listPhoto: List<PhotoItem>)->Unit) {

    private val mapper = ApiMapper
    private val photosService: PhotosService = retrofit.create(PhotosService::class.java)


//    fun getPhotos(query: String, page: Int = 1): List<PhotoItem> {
//        val callHits = photosService.getPhotos(query, page)
//        val listHits = callHits.execute().body()
//        callHits.enqueue(object : Callback<HitsResponseApi>{
//            override fun onResponse(
//                call: Call<HitsResponseApi>,
//                response: Response<HitsResponseApi>,
//            ) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onFailure(call: Call<HitsResponseApi>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })
//        listHits?.let { getPhotos ->
//            return mapper.mapFromHitList(getPhotos.hits)
//        }
//        return emptyList()
//    }

    fun loadPhotos(query: String, page: Int = 1){
        val callHits = photosService.getPhotos(query, page)
//        val listHits = callHits.execute().body()
        callHits.enqueue(object : Callback<HitsResponseApi>{
            override fun onResponse(
                call: Call<HitsResponseApi>,
                response: Response<HitsResponseApi>,
            ) {
                val listHits = response.body()
                listHits?.let { getPhotos ->
                    onPhotoLoad(mapper.mapFromHitList(getPhotos.hits))
                }
                onPhotoLoad(emptyList())
            }

            override fun onFailure(call: Call<HitsResponseApi>, t: Throwable) {
                onPhotoLoad(emptyList())
            }
        })
    }

}