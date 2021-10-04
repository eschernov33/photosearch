package com.evgenii.searchphoto.data.repository

import com.evgenii.searchphoto.data.model.HitApiList
import com.evgenii.searchphoto.data.service.PhotosService
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import retrofit2.Call

class PhotoSearchRepositoryImpl(private val photosService: PhotosService) : PhotoSearchRepository {

    override fun getPhotos(query: String, page: Int): Call<HitApiList> {
        return photosService.getPhotos(query, page)
    }
}