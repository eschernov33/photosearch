package com.evgenii.searchphoto.presentation.datasource

import androidx.paging.DataSource
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository

class DataSourceFactory(
    private val photoSearchRepository: PhotoSearchRepository,
    private val query: String,
    private val onError: () -> Unit,
    private val onLoad: () -> Unit,
) : DataSource.Factory<Int, PhotoItem>() {

    override fun create(): DataSource<Int, PhotoItem> {
        return PhotoListDataSource(photoSearchRepository, query, onError, onLoad)
    }
}