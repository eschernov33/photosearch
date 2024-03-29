package com.evgenii.searchphoto.presentation.datasource

import androidx.paging.PageKeyedDataSource
import com.evgenii.searchphoto.domain.model.LoadResult
import com.evgenii.searchphoto.domain.model.Photo
import com.evgenii.searchphoto.domain.usecases.LoadAfterPhotoListUseCase
import com.evgenii.searchphoto.domain.usecases.LoadInitialPhotoListUseCase

class PhotoListDataSource(
    private val loadInitialPhotoListUseCase: LoadInitialPhotoListUseCase,
    private val loadAfterPhotoListUseCase: LoadAfterPhotoListUseCase,
    private val query: String,
    private val onLoadResult: (result: LoadResult) -> Unit
) : PageKeyedDataSource<Int, Photo>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Photo>
    ) =
        loadInitialPhotoListUseCase.execute(callback, query, FIRST_PAGE, onLoadResult)

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) {}

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Photo>) =
        loadAfterPhotoListUseCase.execute(callback, query, params.key + ONE_PAGE, onLoadResult)

    companion object {
        private const val FIRST_PAGE = 1
        private const val ONE_PAGE = 1
    }
}