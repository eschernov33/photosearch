package com.evgenii.searchphoto.domain.usecases

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.evgenii.searchphoto.domain.model.Photo
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import javax.inject.Inject

class GetPhotoListUseCase @Inject constructor(
    private val photoSearchRepository: PhotoSearchRepository
) {

    operator fun invoke(query: String): LiveData<PagingData<Photo>> =
        photoSearchRepository.getPhotos(query)
}