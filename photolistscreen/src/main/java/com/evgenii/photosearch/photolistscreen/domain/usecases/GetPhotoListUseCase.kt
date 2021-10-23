package com.evgenii.photosearch.photolistscreen.domain.usecases

import androidx.paging.Pager
import com.evgenii.photosearch.core.domain.model.Photo
import com.evgenii.photosearch.photolistscreen.domain.repository.PhotoSearchRepository
import javax.inject.Inject

class GetPhotoListUseCase @Inject constructor(
    private val photoSearchRepository: PhotoSearchRepository
) {

    operator fun invoke(query: String): Pager<Int, Photo> =
        photoSearchRepository.getPhotos(query)
}