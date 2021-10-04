package com.evgenii.searchphoto.presentation.datasource

import androidx.paging.DataSource
import com.evgenii.searchphoto.domain.mapper.ApiMapper
import com.evgenii.searchphoto.domain.model.LoadResult
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository

class DataSourceFactory<T>(
    private val photoSearchRepository: PhotoSearchRepository<T>,
    private val mapper: ApiMapper<T>,
    private val query: String,
    private val onLoadResult: (result: LoadResult) -> Unit,
) : DataSource.Factory<Int, PhotoItem>() {

    override fun create(): DataSource<Int, PhotoItem> {
        return PhotoListDataSource(photoSearchRepository, mapper, query, onLoadResult)
    }
}