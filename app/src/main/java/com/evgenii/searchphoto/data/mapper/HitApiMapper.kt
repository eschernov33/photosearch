package com.evgenii.searchphoto.data.mapper

import com.evgenii.core.data.model.HitApi
import com.evgenii.core.data.model.HitApiResponse
import com.evgenii.searchphoto.domain.model.Photo
import com.evgenii.searchphoto.domain.model.PhotoDetail
import javax.inject.Inject

class HitApiMapper @Inject constructor() {

    fun mapHitApiResponseToListPhoto(hitApiResponse: HitApiResponse): List<Photo> =
        hitApiResponse.hits.map(this::mapHitApiToPhoto)

    fun mapHitApiResponseToListPhotoDetail(hitApiResponse: HitApiResponse): List<PhotoDetail> =
        hitApiResponse.hits.map(this::mapHitApiToPhotoDetail)

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