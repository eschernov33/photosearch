package com.evgenii.photosearch.core.data.pagesource

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.mapper.HitMapper
import com.evgenii.photosearch.core.data.model.HitResponse
import com.evgenii.photosearch.core.domain.model.Photo
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PhotoListRxPageSource(
    private val api: PhotosApi,
    private val query: String,
    private val mapper: HitMapper,
) : RxPagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        val anchor: Int = state.anchorPosition ?: return null
        val page: LoadResult.Page<Int, Photo> = state.closestPageToPosition(anchor) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Photo>> {
        val page: Int = params.key ?: 1
        val prevKey: Int? = if (page == 1) null else page - 1
        val res: Single<HitResponse> = api.getPhotos(query, page)
        return res
            .subscribeOn(Schedulers.io())
            .map<LoadResult<Int, Photo>> { result ->
                val nextKey = if (result.hits.isEmpty()) null else page + 1
                LoadResult.Page(
                    data = mapper.mapHitResponseToListPhoto(result),
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
            .onErrorReturn { throwable ->
                Timber.e(throwable)
                LoadResult.Error(throwable)
            }
    }
}