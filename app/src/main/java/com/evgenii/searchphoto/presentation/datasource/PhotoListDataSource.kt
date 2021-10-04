package com.evgenii.searchphoto.presentation.datasource

import androidx.paging.PageKeyedDataSource
import com.evgenii.searchphoto.domain.mapper.ApiMapper
import com.evgenii.searchphoto.domain.model.LoadResult
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import com.evgenii.searchphoto.domain.usecases.LoadAfterPhotoListUseCase
import com.evgenii.searchphoto.domain.usecases.LoadInitialPhotoListUseCase

class PhotoListDataSource<T>(
    photoSearchRepository: PhotoSearchRepository<T>,
    mapperImpl: ApiMapper<T>,
    private val query: String,
    private val onLoadResult: (result: LoadResult) -> Unit
) : PageKeyedDataSource<Int, PhotoItem>() {

    private val loadInitialPhotoListUseCase =
        LoadInitialPhotoListUseCase(photoSearchRepository, mapperImpl)
    private val loadAfterPhotoListUseCase =
        LoadAfterPhotoListUseCase(photoSearchRepository, mapperImpl)

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, PhotoItem>,
    ) =
        loadInitialPhotoListUseCase.execute(callback, query, FIRST_PAGE, onLoadResult)

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) {}

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PhotoItem>) =
        loadAfterPhotoListUseCase.execute(callback, query, params.key + 1, onLoadResult)

    companion object {
        private const val FIRST_PAGE = 1
    }
}