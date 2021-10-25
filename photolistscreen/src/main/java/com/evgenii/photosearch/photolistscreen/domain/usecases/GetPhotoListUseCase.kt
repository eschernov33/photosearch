package com.evgenii.photosearch.photolistscreen.domain.usecases

import com.evgenii.photosearch.core.domain.model.Photo
import com.evgenii.photosearch.photolistscreen.domain.repository.PhotoSearchRepository
import javax.inject.Inject

class GetPhotoListUseCase @Inject constructor(
    private val photoSearchRepository: PhotoSearchRepository
) {

    suspend operator fun invoke(query: String, page: Int): List<Photo>? =
        photoSearchRepository.getPhotos(query, page)
}