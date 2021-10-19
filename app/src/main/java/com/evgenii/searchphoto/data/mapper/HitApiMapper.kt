package com.evgenii.searchphoto.data.mapper

import com.evgenii.searchphoto.data.model.HitApiItem
import com.evgenii.searchphoto.data.model.HitApiItemList
import com.evgenii.searchphoto.domain.model.Photo
import com.evgenii.searchphoto.domain.model.PhotoDetail
import javax.inject.Inject

class HitApiMapper @Inject constructor() {

    fun mapHitApiItemListToEntities(hitApiItems: HitApiItemList): List<Photo> {
        return hitApiItems.hits.map(this::mapHitApiItemToEntity)
    }

    fun mapHitApiItemListToPhotoDetail(hitApiItems: HitApiItemList): List<PhotoDetail> {
        return hitApiItems.hits.map(this::mapHitApiItemToPhotoDetail)
    }

    private fun mapHitApiItemToPhotoDetail(hitApiItem: HitApiItem) : PhotoDetail =
        PhotoDetail(
            hitApiItem.id,
            hitApiItem.user,
            hitApiItem.userImageURL,
            hitApiItem.likes,
            hitApiItem.downloads,
            hitApiItem.largeImageURL,
            hitApiItem.tags,
            hitApiItem.comments,
            hitApiItem.views,
            hitApiItem.pageURL
        )

    private fun mapHitApiItemToEntity(hitApiItem: HitApiItem): Photo =
        Photo(
            hitApiItem.id,
            hitApiItem.user,
            hitApiItem.userImageURL,
            hitApiItem.likes,
            hitApiItem.downloads,
            hitApiItem.largeImageURL,
            hitApiItem.tags
        )
}