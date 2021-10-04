package com.evgenii.searchphoto.data.repository

import com.evgenii.searchphoto.data.mapper.ApiMapper
import com.evgenii.searchphoto.data.model.HitApiList
import com.evgenii.searchphoto.data.service.PhotosService
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PhotoSearchRepositoryImpl(private val photosService: PhotosService) : PhotoSearchRepository {

    private val mapper = ApiMapper()

    override fun getPhotos(
        query: String,
        page: Int,
        onResponse: (List<PhotoItem>) -> Unit,
        onFailure: (t: Throwable) -> Unit
    ) {
        val responseCall = photosService.getPhotos(query, page)
        responseCall.enqueue(object : Callback<HitApiList> {
            override fun onResponse(call: Call<HitApiList>, response: Response<HitApiList>) {
                val responsePhotoList = response.body()
                if (responsePhotoList == null) {
                    onResponse(emptyList())
                } else {
                    val photoList = mapper.mapHitApiListToEntity(responsePhotoList)
                    onResponse(photoList)
                }
            }

            override fun onFailure(call: Call<HitApiList>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}