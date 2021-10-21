package com.evgenii.photosearch.photolistscreen.domain.usecases

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.evgenii.core.domain.model.Photo
import com.evgenii.photosearch.photolistscreen.domain.repository.PhotoSearchRepository
import javax.inject.Inject

class GetPhotoListUseCase @Inject constructor(
    private val photoSearchRepository: PhotoSearchRepository
) {

    operator fun invoke(query: String): LiveData<PagingData<Photo>> =
        photoSearchRepository.getPhotos(query)
}