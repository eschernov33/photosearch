package com.evgenii.photoofday.presentation.datasource

import androidx.paging.DataSource
import com.evgenii.photoofday.domain.model.PhotoItem
import com.evgenii.photoofday.data.service.PhotosService
import com.evgenii.photoofday.domain.usecases.GetPhotosUseCase

class DataSourceFactory(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val query: String,
) : DataSource.Factory<Int, PhotoItem>() {

    override fun create(): DataSource<Int, PhotoItem> {
        return PhotoListDataSource(getPhotosUseCase, query)
    }
}