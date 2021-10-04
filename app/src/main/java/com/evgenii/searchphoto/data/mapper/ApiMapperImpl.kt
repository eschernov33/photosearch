package com.evgenii.searchphoto.data.mapper

import com.evgenii.searchphoto.data.model.HitApi
import com.evgenii.searchphoto.domain.mapper.ApiMapper
import com.evgenii.searchphoto.domain.model.PhotoItem

class ApiMapperImpl : ApiMapper<HitApi> {

    override fun mapFromEntity(entityApi: HitApi): PhotoItem =
        PhotoItem(
            entityApi.id,
            entityApi.user,
            entityApi.userImageURL,
            entityApi.likes,
            entityApi.downloads,
            entityApi.largeImageURL,
            entityApi.tags
        )

    override fun mapFromEntityList(entityApiList: List<HitApi>): List<PhotoItem> =
        entityApiList.map(this::mapFromEntity)
}