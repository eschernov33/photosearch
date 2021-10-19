package com.evgenii.searchphoto.domain.usecases

import com.evgenii.searchphoto.domain.model.PhotoDetail
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import io.reactivex.Single
import javax.inject.Inject

class GetPhotoByIdUseCase @Inject constructor(
    private val photoSearchRepository: PhotoSearchRepository
) {

    operator fun invoke(photoId: Int): Single<List<PhotoDetail>> =
        photoSearchRepository.getPhotoById(photoId)
}