package com.evgenii.searchphoto.presentation.datasource

import androidx.paging.PageKeyedDataSource
import com.evgenii.searchphoto.data.model.HitsResponseApi
import com.evgenii.searchphoto.domain.mapper.ApiMapper
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import com.evgenii.searchphoto.domain.usecases.LoadPhotoListUseCase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoListDataSource(
    photoSearchRepository: PhotoSearchRepository,
    private val query: String,
    private val onError: () -> Unit,
    private val onLoad: () -> Unit,
) : PageKeyedDataSource<Int, PhotoItem>() {

    private val loadPhotoListUseCase = LoadPhotoListUseCase(photoSearchRepository)
    private val mapper = ApiMapper

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PhotoItem>,
    ) {
        val photoListCall = loadPhotoListUseCase.execute(query, 1)
        photoListCall.enqueue(object : Callback<HitsResponseApi> {
            override fun onResponse(
                call: Call<HitsResponseApi>,
                response: Response<HitsResponseApi>,
            ) {
                val listHits = response.body()
                if (listHits != null && listHits.hits.isNotEmpty()) {
                    callback.onResult(
                        mapper.mapFromHitList(listHits.hits),
                        null,
                        FIRST_PAGE + 1
                    )
                    onLoad()
                } else {
                    onError()
                    callback.onResult(emptyList(), null, null)
                }
            }

            override fun onFailure(call: Call<HitsResponseApi>, t: Throwable) {
                onError()
                callback.onResult(emptyList(), null, null)
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {}

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {
        val photoListCall = loadPhotoListUseCase.execute(query, params.key + 1)
        photoListCall.enqueue(object : Callback<HitsResponseApi> {
            override fun onResponse(
                call: Call<HitsResponseApi>,
                response: Response<HitsResponseApi>,
            ) {
                val listHits = response.body()
                if (listHits != null && listHits.hits.isNotEmpty()) {
                    callback.onResult(
                        mapper.mapFromHitList(listHits.hits),
                        params.key + 1
                    )
                } else {
                    callback.onResult(emptyList(), null)
                }
            }

            override fun onFailure(call: Call<HitsResponseApi>, t: Throwable) {
                onError()
                callback.onResult(emptyList(), null)
            }
        })
    }

    companion object {
        const val FIRST_PAGE = 1
    }
}