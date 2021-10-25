package com.evgenii.photosearch.photolistscreen.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.pagesource.PhotoListPageSource
import com.evgenii.photosearch.core.domain.model.Photo
import com.evgenii.photosearch.photolistscreen.domain.repository.PhotoSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoSearchRepositoryImpl @Inject constructor(
    private val api: PhotosApi,
    private val pagingConfig: PagingConfig
) : PhotoSearchRepository {

    override fun getPhotos(query: String): Flow<PagingData<Photo>> {
        val pagingSource = PhotoListPageSource(api, query)
        return Pager(pagingConfig) {
            pagingSource
        }.flow
    }
}