package com.evgenii.photoofday.data.mapper

import com.evgenii.photoofday.data.model.HitApi
import com.evgenii.photoofday.domain.model.PhotoItem

object ApiMapper {

    private fun mapFromHit(hitApi: HitApi) =
        PhotoItem(
            hitApi.id,
            hitApi.user,
            hitApi.userImageURL,
            hitApi.likes,
            hitApi.downloads,
            hitApi.largeImageURL,
            hitApi.tags
        )

    fun mapFromHitList(hitApiList: List<HitApi>) =
        hitApiList.map(this::mapFromHit)
}