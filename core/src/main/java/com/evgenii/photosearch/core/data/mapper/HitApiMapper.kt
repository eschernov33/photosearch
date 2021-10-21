package com.evgenii.photosearch.core.data.mapper

import com.evgenii.photosearch.core.data.model.HitApi
import com.evgenii.photosearch.core.data.model.HitApiResponse
import com.evgenii.photosearch.core.domain.model.Photo
import com.evgenii.photosearch.core.domain.model.PhotoDetail
import javax.inject.Inject

class HitApiMapper @Inject constructor() {

    fun mapHitApiResponseToListPhotoDetail(hitApiResponse: HitApiResponse): List<PhotoDetail> =
        hitApiResponse.hits.map(this::mapHitApiToPhotoDetail)

    fun mapHitApiResponseToListPhoto(hitApiResponse: HitApiResponse): List<Photo> =
        hitApiResponse.hits.map(this::mapHitApiToPhoto)

    private fun mapHitApiToPhotoDetail(hitApi: HitApi): PhotoDetail =
        PhotoDetail(
            hitApi.id,
            hitApi.user,
            hitApi.userImageURL,
            hitApi.likes,
            hitApi.downloads,
            hitApi.largeImageURL,
            hitApi.tags,
            hitApi.comments,
            hitApi.views,
            hitApi.pageURL
        )

    private fun mapHitApiToPhoto(hitApi: HitApi): Photo =
        Photo(
            hitApi.id,
            hitApi.user,
            hitApi.userImageURL,
            hitApi.likes,
            hitApi.downloads,
            hitApi.largeImageURL,
            hitApi.tags
        )
}