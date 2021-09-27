package com.evgenii.searchphoto.presentation.datasource

import androidx.paging.PageKeyedDataSource
import com.evgenii.searchphoto.data.mapper.ApiMapper
import com.evgenii.searchphoto.data.model.HitsResponseApi
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.domain.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class PhotoListDataSource(
    retrofit: Retrofit,
    private val query: String,
) : PageKeyedDataSource<Int, PhotoItem>() {

    private val repository: Repository = Repository(retrofit) {

    }
    private val mapper = ApiMapper

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PhotoItem>,
    ) {
        val photos = repository.getPhotosCall(query, 1)
        photos.enqueue(object : Callback<HitsResponseApi> {
            override fun onResponse(
                call: Call<HitsResponseApi>,
                response: Response<HitsResponseApi>,
            ) {
                val listHits = response.body()
                if (listHits != null) {
                    callback.onResult(mapper.mapFromHitList(listHits.hits), null, 2)
                } else {
                    callback.onResult(mapper.mapFromHitList(emptyList()), null, null)
                }
            }

            override fun onFailure(call: Call<HitsResponseApi>, t: Throwable) {
                callback.onResult(mapper.mapFromHitList(emptyList()), null, null)
            }
        })

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {
        val photos = repository.getPhotosCall(query, params.key)
        photos.enqueue(object : Callback<HitsResponseApi> {
            override fun onResponse(
                call: Call<HitsResponseApi>,
                response: Response<HitsResponseApi>,
            ) {
                val listHits = response.body()
                if (listHits != null) {
                    callback.onResult(mapper.mapFromHitList(listHits.hits), params.key + 1)
                } else {
                    callback.onResult(mapper.mapFromHitList(emptyList()), null)
                }
            }

            override fun onFailure(call: Call<HitsResponseApi>, t: Throwable) {
                callback.onResult(mapper.mapFromHitList(emptyList()), null)
            }
        })
    }
}