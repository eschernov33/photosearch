package com.evgenii.searchphoto.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.evgenii.searchphoto.data.source.PhotoListRxPageSource
import com.evgenii.searchphoto.domain.model.Photo
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import javax.inject.Inject

class PhotoSearchRepositoryImpl @Inject constructor(): PhotoSearchRepository {
    override fun getPhotos(
        query: String,
        page: Int,
        pagingSource: PhotoListRxPageSource
    ): LiveData<PagingData<Photo>> {
        Log.i("my", "getPhotos")

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                prefetchDistance = 1
            ),
            pagingSourceFactory = { pagingSource }
        ).liveData
    }
//    override fun getPhotos(
//        query: String,
//        page: Int,
//        onResponse: (List<Photo>) -> Unit,
//        onFailure: (t: Throwable) -> Unit
//    ) {
//        val responseCall = photosApi.getPhotos(query, page)
//        responseCall.enqueue(object : Callback<HitApiItemList> {
//            override fun onResponse(
//                call: Call<HitApiItemList>,
//                response: Response<HitApiItemList>
//            ) {
//                val responsePhotoList = response.body()
//                if (responsePhotoList == null) {
//                    onResponse(emptyList())
//                } else {
//                    val photoList = mapper.mapHitApiItemListToEntities(responsePhotoList)
//                    onResponse(photoList)
//                }
//            }
//
//            override fun onFailure(call: Call<HitApiItemList>, t: Throwable) {
//                onFailure(t)
//            }
//        })
//    }
}