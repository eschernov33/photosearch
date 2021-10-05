package com.evgenii.searchphoto.data.mapper

import com.evgenii.searchphoto.data.model.HitApiItem
import com.evgenii.searchphoto.data.model.HitApiItemList
import com.evgenii.searchphoto.domain.model.Photo

class HitApiMapper {

    fun mapHitApiItemListToEntities(hitApiItems: HitApiItemList): List<Photo> =
        hitApiItems.hits.map(this::mapHitApiItemToEntity)

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