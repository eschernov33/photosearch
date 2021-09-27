package com.evgenii.searchphoto.domain.repository

import androidx.paging.PageKeyedDataSource
import com.evgenii.searchphoto.data.mapper.ApiMapper
import com.evgenii.searchphoto.data.model.HitsResponseApi
import com.evgenii.searchphoto.data.service.PhotosService
import com.evgenii.searchphoto.domain.model.PhotoItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class Repository(retrofit: Retrofit) {

    private val photosService: PhotosService = retrofit.create(PhotosService::class.java)
    private val mapper = ApiMapper

    fun loadInitialPhotoList(
        query: String,
        callback: PageKeyedDataSource.LoadInitialCallback<Int, PhotoItem>,
    ) {
        val callHitsResponseApi = photosService.getPhotos(query, FIRST_PAGE)
        callHitsResponseApi.enqueue(object : Callback<HitsResponseApi> {
            override fun onResponse(
                call: Call<HitsResponseApi>,
                response: Response<HitsResponseApi>,
            ) {
                val listHits = response.body()
                if (listHits != null) {
                    callback.onResult(
                        mapper.mapFromHitList(listHits.hits),
                        null,
                        FIRST_PAGE + 1
                    )
                } else {
                    callback.onResult(emptyList(), null, null)
                }
            }

            override fun onFailure(call: Call<HitsResponseApi>, t: Throwable) {
                callback.onResult(emptyList(), null, null)
            }
        })
    }

    fun loadAfterPhotoList(
        query: String,
        page: Int,
        callback: PageKeyedDataSource.LoadCallback<Int, PhotoItem>,
    ) {
        val callHitsResponseApi = photosService.getPhotos(query, page)
        callHitsResponseApi.enqueue(object : Callback<HitsResponseApi> {
            override fun onResponse(
                call: Call<HitsResponseApi>,
                response: Response<HitsResponseApi>,
            ) {
                val listHits = response.body()
                if (listHits != null) {
                    callback.onResult(mapper.mapFromHitList(listHits.hits), page + 1)
                } else {
                    callback.onResult(emptyList(), null)
                }
            }

            override fun onFailure(call: Call<HitsResponseApi>, t: Throwable) {
                callback.onResult(emptyList(), null)
            }
        })
    }

    companion object {
        const val FIRST_PAGE = 1
    }
}