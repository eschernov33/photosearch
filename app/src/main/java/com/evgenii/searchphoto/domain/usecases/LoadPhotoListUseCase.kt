package com.evgenii.searchphoto.domain.usecases

import com.evgenii.searchphoto.data.model.HitsResponseApi
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import retrofit2.Call

class LoadPhotoListUseCase(private val photoSearchRepository: PhotoSearchRepository) {

    fun execute(query: String, page: Int): Call<HitsResponseApi> =
        photoSearchRepository.getPhotos(query, page)
}