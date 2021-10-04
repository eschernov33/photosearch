package com.evgenii.searchphoto.domain.mapper

import com.evgenii.searchphoto.domain.model.PhotoItem

interface ApiMapper<T> {

    fun mapFromEntity(entity: T): List<PhotoItem>
}