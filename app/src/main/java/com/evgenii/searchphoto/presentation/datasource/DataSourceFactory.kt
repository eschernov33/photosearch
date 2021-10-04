package com.evgenii.searchphoto.presentation.datasource

import androidx.paging.DataSource
import com.evgenii.searchphoto.domain.model.LoadResult
import com.evgenii.searchphoto.domain.model.Photo
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository

class DataSourceFactory(
    private val photoSearchRepository: PhotoSearchRepository,
    private val query: String,
    private val onLoadResult: (result: LoadResult) -> Unit,
) : DataSource.Factory<Int, Photo>() {

    override fun create(): DataSource<Int, Photo> {
        return PhotoListDataSource(photoSearchRepository, query, onLoadResult)
    }
}