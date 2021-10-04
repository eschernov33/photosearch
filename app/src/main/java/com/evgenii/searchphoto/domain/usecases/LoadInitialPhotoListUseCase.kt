package com.evgenii.searchphoto.domain.usecases

import androidx.paging.PageKeyedDataSource
import com.evgenii.searchphoto.data.mapper.ApiMapperImpl
import com.evgenii.searchphoto.data.model.HitApiList
import com.evgenii.searchphoto.domain.model.LoadResult
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import com.evgenii.searchphoto.presentation.datasource.PhotoListDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadInitialPhotoListUseCase(
    private val photoSearchRepository: PhotoSearchRepository,
    private val mapper: ApiMapperImpl
) {

    fun execute(
        callback: PageKeyedDataSource.LoadInitialCallback<Int, PhotoItem>,
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
                        null,
                        PhotoListDataSource.FIRST_PAGE + 1
                    )
                    onLoadResult(LoadResult.SUCCESS)
                } else {
                    onLoadResult(LoadResult.EMPTY)
                    callback.onResult(emptyList(), null, null)
                }
            }

            override fun onFailure(call: Call<HitApiList>, t: Throwable) {
                onLoadResult(LoadResult.ERROR)
                callback.onResult(emptyList(), null, null)
            }
        })
    }

}