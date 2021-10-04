package com.evgenii.searchphoto.domain.usecases

import androidx.paging.PageKeyedDataSource
import com.evgenii.searchphoto.data.mapper.ApiMapperImpl
import com.evgenii.searchphoto.data.model.HitApiList
import com.evgenii.searchphoto.domain.model.LoadResult
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadAfterPhotoListUseCase(
    private val photoSearchRepository: PhotoSearchRepository,
    private val mapper: ApiMapperImpl
) {

    fun execute(
        callback: PageKeyedDataSource.LoadCallback<Int, PhotoItem>,
        query: String,
        page: Int,
        onLoadResult: (result: LoadResult) -> Unit
    ) {
        val photoListCall = photoSearchRepository.getPhotos(query, page)
        photoListCall.enqueue(object : Callback<HitApiList> {
            override fun onResponse(
                call: Call<HitApiList>,
                response: Response<HitApiList>,
            ) {
                val listHits = response.body()
                if (listHits != null && listHits.hits.isNotEmpty()) {
                    callback.onResult(
                        mapper.mapFromEntityList(listHits.hits),
                        page + 1
                    )
                } else {
                    callback.onResult(emptyList(), null)
                }
            }

            override fun onFailure(call: Call<HitApiList>, t: Throwable) {
                onLoadResult(LoadResult.ERROR)
                callback.onResult(emptyList(), null)
            }
        })
    }
}