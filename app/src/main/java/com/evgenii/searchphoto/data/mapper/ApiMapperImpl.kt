package com.evgenii.searchphoto.data.mapper

import com.evgenii.searchphoto.data.model.HitApi
import com.evgenii.searchphoto.data.model.HitApiList
import com.evgenii.searchphoto.domain.mapper.ApiMapper
import com.evgenii.searchphoto.domain.model.PhotoItem

class ApiMapperImpl : ApiMapper<HitApiList> {

    private fun mapFromItemEntity(entityApi: HitApi): PhotoItem =
        PhotoItem(
            entityApi.id,
            entityApi.user,
            entityApi.userImageURL,
            entityApi.likes,
            entityApi.downloads,
            entityApi.largeImageURL,
            entityApi.tags
        )

    private fun mapFromItemEntityList(entityApiList: List<HitApi>): List<PhotoItem> =
        entityApiList.map(this::mapFromItemEntity)

    override fun mapFromEntity(entity: HitApiList): List<PhotoItem> =
        mapFromItemEntityList(entity.hits)

}