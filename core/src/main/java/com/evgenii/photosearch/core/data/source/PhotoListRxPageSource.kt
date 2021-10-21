package com.evgenii.photosearch.core.data.source

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.mapper.HitApiMapper
import com.evgenii.photosearch.core.domain.model.Photo
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PhotoListRxPageSource(
    private val api: PhotosApi,
    private val query: String,
    private val mapper: HitApiMapper,
) : RxPagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Photo>> {
        val page = params.key ?: 1
        val prevKey = if (page == 1) null else page - 1
        val res = api.getPhotos(query, page)
        return res
            .subscribeOn(Schedulers.io())
            .map<LoadResult<Int, Photo>> { result ->
                val nextKey = if (result.hits.isEmpty()) null else page + 1
                LoadResult.Page(
                    data = mapper.mapHitApiResponseToListPhoto(result),
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