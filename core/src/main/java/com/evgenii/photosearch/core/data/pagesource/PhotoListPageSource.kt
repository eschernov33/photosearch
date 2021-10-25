package com.evgenii.photosearch.core.data.pagesource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.mapper.HitMapper
import com.evgenii.photosearch.core.data.model.HitResponse
import com.evgenii.photosearch.core.domain.model.Photo
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

class PhotoListPageSource(
    private val api: PhotosApi,
    private val query: String,
    private val mapper: HitMapper = HitMapper()
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

        try {
            val page: Int = params.key ?: 1
            val loadSize: Int = params.loadSize
            val prevKey: Int? = if (page == 1) null else page - 1
            val response: Response<HitResponse> = api.getPhotos(query, page)

            return if (response.isSuccessful) {
                val hitResponse: HitResponse? = response.body()
                if (hitResponse == null) {
                    Timber.e(HttpException(response))
                    LoadResult.Error(HttpException(response))
                } else {
                    val photoList = mapper.mapHitResponseToListPhoto(hitResponse)
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