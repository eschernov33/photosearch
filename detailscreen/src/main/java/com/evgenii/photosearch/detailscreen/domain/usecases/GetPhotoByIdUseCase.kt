package com.evgenii.photosearch.detailscreen.domain.usecases

import com.evgenii.photosearch.core.domain.model.PhotoDetail
import com.evgenii.photosearch.detailscreen.domain.repository.PhotoSearchDetailRepository
import io.reactivex.Single
import javax.inject.Inject

internal class GetPhotoByIdUseCase @Inject constructor(
    private val photoSearchRepository: PhotoSearchDetailRepository
) {

    operator fun invoke(photoId: Int): Single<List<PhotoDetail>> =
        photoSearchRepository.getPhotoById(photoId)
}