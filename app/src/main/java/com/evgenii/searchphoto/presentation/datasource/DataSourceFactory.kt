package com.evgenii.searchphoto.presentation.datasource

import androidx.paging.DataSource
import com.evgenii.searchphoto.domain.model.PhotoItem
import retrofit2.Retrofit

class DataSourceFactory(
    private val retrofit: Retrofit,
    private val query: String,
) : DataSource.Factory<Int, PhotoItem>() {

    override fun create(): DataSource<Int, PhotoItem> {
        return PhotoListDataSource(retrofit, query)
    }
}