package com.evgenii.searchphoto.presentation.datasource

import androidx.paging.DataSource
import com.evgenii.searchphoto.data.mapper.ApiMapperImpl
import com.evgenii.searchphoto.domain.model.LoadResult
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository

class DataSourceFactory(
    private val photoSearchRepository: PhotoSearchRepository,
    private val mapper: ApiMapperImpl,
    private val query: String,
    private val onLoadResult: (result: LoadResult) -> Unit,
) : DataSource.Factory<Int, PhotoItem>() {

    override fun create(): DataSource<Int, PhotoItem> {
        return PhotoListDataSource(photoSearchRepository, mapper, query, onLoadResult)
    }
}