package com.evgenii.searchphoto.data.mapper

import android.util.Log
import com.evgenii.searchphoto.data.model.HitApiItem
import com.evgenii.searchphoto.data.model.HitApiItemList
import com.evgenii.searchphoto.domain.model.Photo

class HitApiMapper {

    fun mapHitApiItemListToEntities(hitApiItems: HitApiItemList): List<Photo> {
        Log.i("my", "size = " + hitApiItems.hits.size)
        return hitApiItems.hits.map(this::mapHitApiItemToEntity)
    }

    fun mapHitApiItemToEntity(hitApiItem: HitApiItem): Photo =
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