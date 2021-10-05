package com.evgenii.searchphoto.presentation.datasource

import androidx.paging.DataSource
import com.evgenii.searchphoto.domain.model.LoadResult
import com.evgenii.searchphoto.domain.model.Photo
import com.evgenii.searchphoto.domain.usecases.LoadAfterPhotoListUseCase
import com.evgenii.searchphoto.domain.usecases.LoadInitialPhotoListUseCase

class DataSourceFactory(
    private val loadInitialPhotoListUseCase: LoadInitialPhotoListUseCase,
    private val loadAfterPhotoListUseCase: LoadAfterPhotoListUseCase,
    private val query: String,
    private val onLoadResult: (result: LoadResult) -> Unit,
) : DataSource.Factory<Int, Photo>() {

    override fun create(): DataSource<Int, Photo> {
        return PhotoListDataSource(
            loadInitialPhotoListUseCase,
            loadAfterPhotoListUseCase,
            query,
            onLoadResult
        )
    }
}