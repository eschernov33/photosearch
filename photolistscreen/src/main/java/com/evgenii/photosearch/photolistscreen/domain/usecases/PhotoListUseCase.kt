package com.evgenii.photosearch.photolistscreen.domain.usecases

import androidx.paging.PagingData
import com.evgenii.photosearch.core.domain.model.Photo
import com.evgenii.photosearch.photolistscreen.domain.repository.PhotoSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoListUseCase @Inject constructor(
    private val photoSearchRepository: PhotoSearchRepository
) {

    suspend fun getPhotos(query: String): Flow<PagingData<Photo>> =
        photoSearchRepository.getPhotos(
            query,
            debugMode = com.evgenii.photosearch.core.BuildConfig.IS_LOCAL_DATA_DEBUG
        )
}