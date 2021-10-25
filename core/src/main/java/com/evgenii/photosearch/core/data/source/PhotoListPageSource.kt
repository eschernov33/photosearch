package com.evgenii.photosearch.core.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.mapper.HitApiMapper
import com.evgenii.photosearch.core.domain.model.Photo
import retrofit2.HttpException
import timber.log.Timber

class PhotoListPageSource(
    private val api: PhotosApi,
    private val query: String,
    private val mapper: HitApiMapper = HitApiMapper()
) : PagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        if (query.isEmpty()) {
            return LoadResult.Page(emptyList(), null, null)
        }

        try {
            val page = params.key ?: 1
            val loadSize = params.loadSize
            val prevKey = if (page == 1) null else page - 1
            val response = api.getPhotos(query, page)

            return if (response.isSuccessful) {
                val hitApiResponse = response.body()
                if (hitApiResponse == null) {
                    Timber.e(HttpException(response))
                    LoadResult.Error(HttpException(response))
                } else {
                    val photoList = mapper.mapHitApiResponseToListPhoto(hitApiResponse)
                    val nextKey = if (photoList.size < loadSize) null else page + 1
                    LoadResult.Page(photoList, prevKey, nextKey)
                }
            } else {
                Timber.e(HttpException(response))
                LoadResult.Error(HttpException(response))
            }
        } catch (exception: Exception) {
            Timber.e(exception)
            return LoadResult.Error(exception)
        }
    }
}