package com.evgenii.photoofday.presentation.datasource

import androidx.paging.PageKeyedDataSource
import com.evgenii.photoofday.data.mapper.ApiMapper
import com.evgenii.photoofday.domain.model.PhotoItem
import com.evgenii.photoofday.domain.usecases.GetPhotosUseCase

class PhotoListDataSource(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val query: String,
) : PageKeyedDataSource<Int, PhotoItem>() {

    private val mapper = ApiMapper

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PhotoItem>,
    ) {
        val photos = getPhotosUseCase.getPhotos(query, 1)
        callback.onResult(photos, null, 2)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {
        val photos = getPhotosUseCase.getPhotos(query, params.key)
//        val listHits = photos.execute().body()
//        listHits?.let { getPhotos ->
//            val photosList = mapper.mapFromHitList(getPhotos.hits)
//            callback.onResult(photosList, params.key + 1)
//        }
        callback.onResult(photos,  params.key + 1)
    }
}