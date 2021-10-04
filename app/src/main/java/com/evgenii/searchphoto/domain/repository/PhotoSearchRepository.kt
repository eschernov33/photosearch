package com.evgenii.searchphoto.domain.repository

import com.evgenii.searchphoto.domain.model.PhotoItem

interface PhotoSearchRepository {

    fun getPhotos(
        query: String,
        page: Int,
        onResponse: (List<PhotoItem>) -> Unit,
        onFailure: (t: Throwable) -> Unit
    )
}