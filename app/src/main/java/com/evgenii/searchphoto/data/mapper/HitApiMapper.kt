package com.evgenii.searchphoto.data.mapper

import com.evgenii.searchphoto.data.model.HitApi
import com.evgenii.searchphoto.data.model.HitApiList
import com.evgenii.searchphoto.domain.model.Photo

class HitApiMapper {

    private fun mapHitApiToEntity(hitApi: HitApi): Photo =
        Photo(
            hitApi.id,
            hitApi.user,
            hitApi.userImageURL,
            hitApi.likes,
            hitApi.downloads,
            hitApi.largeImageURL,
            hitApi.tags
        )

    fun mapHitApiListToEntity(hitApi: HitApiList): List<Photo> =
        hitApi.hits.map(this::mapHitApiToEntity)

}