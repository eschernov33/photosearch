package com.evgenii.photosearch.core.data.mapper

import com.evgenii.photosearch.core.data.model.Hit
import com.evgenii.photosearch.core.data.model.HitResponse
import com.evgenii.photosearch.core.domain.model.Photo
import com.evgenii.photosearch.core.domain.model.PhotoDetail
import javax.inject.Inject

class HitMapper @Inject constructor() {

    fun mapHitResponseToListPhotoDetail(hitResponse: HitResponse): List<PhotoDetail> =
        hitResponse.hits.map(::mapHitToPhotoDetail)

    fun mapHitResponseToListPhoto(hitResponse: HitResponse): List<Photo> =
        hitResponse.hits.map(::mapHitToPhoto)

    private fun mapHitToPhotoDetail(hit: Hit): PhotoDetail =
        PhotoDetail(
            id = hit.id,
            user = hit.user,
            userImageURL = hit.userImageURL,
            likes = hit.likes,
            downloads = hit.downloads,
            largeImageURL = hit.largeImageURL,
            tags = hit.tags,
            comments = hit.comments,
            views = hit.views,
            pageURL = hit.pageURL
        )

    private fun mapHitToPhoto(hit: Hit): Photo =
        Photo(
            id = hit.id,
            user = hit.user,
            userImageURL = hit.userImageURL,
            likes = hit.likes,
            downloads = hit.downloads,
            largeImageURL = hit.largeImageURL,
            tags = hit.tags
        )
}