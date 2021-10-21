package com.evgenii.searchphoto.data.repository

import androidx.paging.PagingConfig
import com.evgenii.core.data.mapper.HitApiMapper
import com.evgenii.core.domain.model.PhotoDetail
import com.evgenii.photosearch.detailscreen.domain.repository.PhotoSearchByIdRepository
import com.evgenii.searchphoto.data.api.PhotosApi

import io.reactivex.Single
import javax.inject.Inject

class PhotoSearchByIdRepositoryImpl @Inject constructor(
    private val api: PhotosApi,
    private val mapper: HitApiMapper,
    private val pagingConfig: PagingConfig
) : PhotoSearchByIdRepository {

    override fun getPhotoById(photoId: Int): Single<List<PhotoDetail>> {
        return api.getPhotoById(photoId).map { mapper.mapHitApiResponseToListPhotoDetail(it) }
    }
}