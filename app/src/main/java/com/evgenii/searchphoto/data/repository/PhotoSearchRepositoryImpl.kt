package com.evgenii.searchphoto.data.repository

import com.evgenii.searchphoto.data.model.HitsResponseApi
import com.evgenii.searchphoto.data.service.PhotosService
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import retrofit2.Call

class PhotoSearchRepositoryImpl(private val photosService: PhotosService) : PhotoSearchRepository {

    override fun getPhotos(query: String, page: Int): Call<HitsResponseApi> {
        return photosService.getPhotos(query, page)
    }
}