package com.evgenii.photosearch.photolistscreen.data.pagesource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.evgenii.photosearch.core.data.model.PhotoApiResponse
import com.evgenii.photosearch.core.domain.model.Photo
import com.evgenii.photosearch.photolistscreen.data.api.PhotoListApi
import com.evgenii.photosearch.photolistscreen.data.mapper.PhotosApiMapper
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber

class PhotoListPageSource(
    private val api: PhotoListApi,
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

        try {
            val page: Int = params.key ?: 1
            val loadSize: Int = params.loadSize
            val prevKey: Int? = if (page == 1) null else page - 1
            val response: Response<PhotoApiResponse> = api.getPhotos(query, page)

            return if (response.isSuccessful) {
                val photoApiResponse: PhotoApiResponse? = response.body()
                if (photoApiResponse == null) {
                    Timber.e(HttpException(response))
                    LoadResult.Error(HttpException(response))
                } else {
                    val photoList = mapper.mapPhotoApiResponseToListPhoto(photoApiResponse)
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