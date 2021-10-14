package com.evgenii.searchphoto.domain.usecases

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.evgenii.searchphoto.data.api.PhotosApi
import com.evgenii.searchphoto.data.source.PhotoListRxPageSource
import com.evgenii.searchphoto.domain.model.Photo
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import javax.inject.Inject

class LoadPhotoListUseCase @Inject constructor(
    private val photoSearchRepository: PhotoSearchRepository,
) {

    fun execute(
//        callback: PageKeyedDataSource.LoadCallback<Int, Photo>,
        photosApi: PhotosApi,
        query: String,
        page: Int,
        photoListRxPageSource: PhotoListRxPageSource
//        onLoadResult: (result: LoadResult) -> Unit
    ): LiveData<PagingData<Photo>> {
//        val onResponse: (List<Photo>) -> Unit = { photoList ->
//            if (photoList.isEmpty()) {
//                callback.onResult(emptyList(), null)
//            } else {
//                callback.onResult(photoList, page + ONE_PAGE)
//            }
//
//        }
//        val onFailure: (t: Throwable) -> Unit = {
//            onLoadResult(LoadResult.ERROR)
//            callback.onResult(emptyList(), null)
//        }
        Log.e("my", "phoooo")
        val res = photoSearchRepository.getPhotos(query, page, photoListRxPageSource)
        Log.e("my", "res = " + res.toString())
        return res
    }

    companion object {
        private const val ONE_PAGE = 1
    }
}