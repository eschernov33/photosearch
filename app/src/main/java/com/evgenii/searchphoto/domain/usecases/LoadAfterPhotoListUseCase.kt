package com.evgenii.searchphoto.domain.usecases

import androidx.paging.PageKeyedDataSource
import com.evgenii.searchphoto.domain.mapper.ApiMapper
import com.evgenii.searchphoto.domain.model.LoadResult
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadAfterPhotoListUseCase<T>(
    private val photoSearchRepository: PhotoSearchRepository<T>,
    private val mapper: ApiMapper<T>
) {

    fun execute(
        callback: PageKeyedDataSource.LoadCallback<Int, PhotoItem>,
        query: String,
        page: Int,
        onLoadResult: (result: LoadResult) -> Unit
    ) {
        val photoListCall = photoSearchRepository.getPhotos(query, page)
        photoListCall.enqueue(object : Callback<T> {
            override fun onResponse(
                call: Call<T>,
                response: Response<T>,
            ) {
                val responsePhotoList = response.body()
                if (responsePhotoList == null) {
                    callback.onResult(emptyList(), null)
                } else {
                    val photoList = mapper.mapFromEntity(responsePhotoList)
                    if (photoList.isNotEmpty()) {
                        callback.onResult(photoList, page + 1)
                    } else {
                        callback.onResult(emptyList(), null)
                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                onLoadResult(LoadResult.ERROR)
                callback.onResult(emptyList(), null)
            }
        })
    }
}