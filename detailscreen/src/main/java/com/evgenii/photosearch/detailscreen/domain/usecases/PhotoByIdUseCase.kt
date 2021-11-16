package com.evgenii.photosearch.detailscreen.domain.usecases

import com.evgenii.photosearch.core.BuildConfig
import com.evgenii.photosearch.detailscreen.domain.model.PhotoDetail
import com.evgenii.photosearch.detailscreen.domain.repository.PhotoSearchDetailRepository
import javax.inject.Inject

class PhotoByIdUseCase @Inject constructor(
    private val photoSearchRepository: PhotoSearchDetailRepository
) {

    suspend fun getPhoto(photoId: Int): PhotoDetail? =
        photoSearchRepository.getPhotoById(
            photoId,
            debugMode = BuildConfig.IS_LOCAL_DATA_DEBUG
        )
}