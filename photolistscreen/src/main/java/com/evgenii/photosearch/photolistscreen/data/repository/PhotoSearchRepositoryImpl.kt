package com.evgenii.photosearch.photolistscreen.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.evgenii.photosearch.core.domain.model.Photo
import com.evgenii.photosearch.photolistscreen.data.api.PhotoListApi
import com.evgenii.photosearch.photolistscreen.data.pagesource.PhotoListForDebugPageSource
import com.evgenii.photosearch.photolistscreen.data.pagesource.PhotoListPageSource
import com.evgenii.photosearch.photolistscreen.domain.repository.PhotoSearchRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoSearchRepositoryImpl @Inject constructor(
    private val api: PhotoListApi,
    private val pagingConfig: PagingConfig
) : PhotoSearchRepository {

    override suspend fun getPhotos(query: String, debugMode: Boolean): Flow<PagingData<Photo>> {
        val pageSource = if (debugMode) {
            delay(500)
            PhotoListForDebugPageSource(query)
        } else {
            PhotoListPageSource(api, query)
        }
        return Pager(pagingConfig) {
            pageSource
        }.flow
    }
}