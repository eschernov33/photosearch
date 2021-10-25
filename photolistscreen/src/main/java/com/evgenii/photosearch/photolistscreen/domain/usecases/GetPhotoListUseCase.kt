package com.evgenii.photosearch.photolistscreen.domain.usecases

import androidx.paging.PagingData
import com.evgenii.photosearch.core.domain.model.Photo
import com.evgenii.photosearch.photolistscreen.domain.repository.PhotoSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotoListUseCase @Inject constructor(
    private val photoSearchRepository: PhotoSearchRepository
) {

    operator fun invoke(query: String): Flow<PagingData<Photo>> =
        photoSearchRepository.getPhotos(query)
}