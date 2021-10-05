package com.evgenii.searchphoto.data.repository

import com.evgenii.searchphoto.data.api.PhotosApi
import com.evgenii.searchphoto.data.mapper.HitApiMapper
import com.evgenii.searchphoto.data.model.HitApiItemList
import com.evgenii.searchphoto.domain.model.Photo
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoSearchRepositoryImpl(
    private val mapper: HitApiMapper,
    private val photosApi: PhotosApi
) : PhotoSearchRepository {

    override fun getPhotos(
        query: String,
        page: Int,
        onResponse: (List<Photo>) -> Unit,
        onFailure: (t: Throwable) -> Unit
    ) {
        val responseCall = photosApi.getPhotos(query, page)
        responseCall.enqueue(object : Callback<HitApiItemList> {
            override fun onResponse(
                call: Call<HitApiItemList>,
                response: Response<HitApiItemList>
            ) {
                val responsePhotoList = response.body()
                if (responsePhotoList == null) {
                    onResponse(emptyList())
                } else {
                    val photoList = mapper.mapHitApiItemListToEntities(responsePhotoList)
                    onResponse(photoList)
                }
            }

            override fun onFailure(call: Call<HitApiItemList>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}