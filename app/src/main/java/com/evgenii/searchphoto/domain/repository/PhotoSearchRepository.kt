package com.evgenii.searchphoto.domain.repository

import com.evgenii.searchphoto.domain.model.Photo

interface PhotoSearchRepository {

    fun getPhotos(
        query: String,
        page: Int,
        onResponse: (List<Photo>) -> Unit,
        onFailure: (t: Throwable) -> Unit
    )
}