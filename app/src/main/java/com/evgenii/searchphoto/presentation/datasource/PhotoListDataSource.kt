package com.evgenii.searchphoto.presentation.datasource

import androidx.paging.PageKeyedDataSource
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.domain.repository.Repository
import retrofit2.Retrofit

class PhotoListDataSource(
    retrofit: Retrofit,
    private val query: String,
) : PageKeyedDataSource<Int, PhotoItem>() {

    private val repository: Repository = Repository(retrofit)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PhotoItem>,
    ) =
        repository.loadInitialPhotoList(query, callback)

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {

    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) =
        repository.loadAfterPhotoList(query, params.key, callback)
}