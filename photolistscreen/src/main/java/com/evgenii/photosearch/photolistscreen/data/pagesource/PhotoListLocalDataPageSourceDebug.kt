package com.evgenii.photosearch.photolistscreen.data.pagesource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.evgenii.photosearch.core.data.datasource.DebugData
import com.evgenii.photosearch.core.data.model.PhotoApiResponse
import com.evgenii.photosearch.core.domain.model.Photo
import com.evgenii.photosearch.photolistscreen.data.mapper.PhotosApiMapper
import kotlinx.coroutines.delay

class PhotoListForDebugPageSource(
    private val query: String,
    private val mapper: PhotosApiMapper = PhotosApiMapper()
) : PagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        val anchor: Int = state.anchorPosition ?: return null
        val page: LoadResult.Page<Int, Photo> = state.closestPageToPosition(anchor) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        if (query.isEmpty()) {
            return LoadResult.Page(emptyList(), null, null)
        }
        val page: Int = params.key ?: 1
        val loadSize: Int = params.loadSize
        val prevKey: Int? = if (page == 1) null else page - 1
        val response = PhotoApiResponse(photoApiItems = DebugData().getDebugPhotoList())
        delay(500)
        val photoList = mapper.mapPhotoApiResponseToListPhoto(response)
        val nextKey = if (photoList.size < loadSize) null else page + 1
        return LoadResult.Page(photoList, prevKey, nextKey)
    }
}