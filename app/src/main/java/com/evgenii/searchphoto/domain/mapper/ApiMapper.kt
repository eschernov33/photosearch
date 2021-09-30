package com.evgenii.searchphoto.domain.mapper

import com.evgenii.searchphoto.data.model.HitApi
import com.evgenii.searchphoto.domain.model.PhotoItem

object ApiMapper {

    private fun mapFromHit(hitApi: HitApi): PhotoItem =
        PhotoItem(
            hitApi.id,
            hitApi.user,
            hitApi.userImageURL,
            hitApi.likes,
            hitApi.downloads,
            hitApi.largeImageURL,
            hitApi.tags
        )

    fun mapFromHitList(hitApiList: List<HitApi>): List<PhotoItem> =
        hitApiList.map(this::mapFromHit)

//    fun mapFromService(call: Call<HitsResponseApi>): Call<PhotoItem>{
//
//    }
}