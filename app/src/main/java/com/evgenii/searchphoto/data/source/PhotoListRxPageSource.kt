package com.evgenii.searchphoto.data.source

import android.util.Log
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.evgenii.searchphoto.data.api.PhotosApi
import com.evgenii.searchphoto.data.mapper.HitApiMapper
import com.evgenii.searchphoto.domain.model.Photo
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class PhotoListRxPageSource(
    private val api: PhotosApi,
    private val query: String,
) : RxPagingSource<Int, Photo>() {

    private val mapper = HitApiMapper()

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
//        return state.anchorPosition?.let { state.closestItemToPosition(it)?.id }
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Photo>> {
        Log.i("my", "loadSingle")
        val page = params.key ?: 1
        val prevKey = if (page == 1) null else page - 1
        val res = api.getPhotos(query, page)

        return res
            .subscribeOn(Schedulers.io())
            .map<LoadResult<Int, Photo>> { result ->
                Log.e("my", result.toString())
                val nextKey = if (result.hits.isEmpty()) null else page + 1
                LoadResult.Page(
                    data = mapper.mapHitApiItemListToEntities(result),
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            }
            .onErrorReturn {
                LoadResult.Error(it)
            }
        //.map { mapper.mapHitApiItemListToEntities(it) }
//            .map { LoadResult.Page(it, prevKey, page + 1) }
    }
}