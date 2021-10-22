package com.evgenii.photosearch.core.data.mapper

import com.evgenii.photosearch.core.data.model.HitApi
import com.evgenii.photosearch.core.data.model.HitApiResponse
import com.evgenii.photosearch.core.domain.model.Photo
import com.evgenii.photosearch.core.domain.model.PhotoDetail
import javax.inject.Inject

class HitApiMapper @Inject constructor() {

    fun mapHitApiResponseToListPhotoDetail(hitApiResponse: HitApiResponse): List<PhotoDetail> =
        hitApiResponse.hits.map(::mapHitApiToPhotoDetail)

    fun mapHitApiResponseToListPhoto(hitApiResponse: HitApiResponse): List<Photo> =
        hitApiResponse.hits.map(::mapHitApiToPhoto)

    private fun mapHitApiToPhotoDetail(hitApi: HitApi): PhotoDetail =
        PhotoDetail(
            id = hitApi.id,
            user = hitApi.user,
            userImageURL = hitApi.userImageURL,
            likes = hitApi.likes,
            downloads = hitApi.downloads,
            largeImageURL = hitApi.largeImageURL,
            tags = hitApi.tags,
            comments = hitApi.comments,
            views = hitApi.views,
            pageURL = hitApi.pageURL
        )

    private fun mapHitApiToPhoto(hitApi: HitApi): Photo =
        Photo(
            id = hitApi.id,
            user = hitApi.user,
            userImageURL = hitApi.userImageURL,
            likes = hitApi.likes,
            downloads = hitApi.downloads,
            largeImageURL = hitApi.largeImageURL,
            tags = hitApi.tags
        )
}