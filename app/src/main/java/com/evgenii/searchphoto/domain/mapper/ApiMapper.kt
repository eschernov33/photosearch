package com.evgenii.searchphoto.domain.mapper

import com.evgenii.searchphoto.domain.model.PhotoItem

interface ApiMapper<T> {

    fun mapFromEntity(entityApi: T): PhotoItem

    fun mapFromEntityList(entityApiList: List<T>): List<PhotoItem>
}