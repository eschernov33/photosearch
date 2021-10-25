package com.evgenii.photosearch.photolistscreen.domain.repository

import com.evgenii.photosearch.core.domain.model.Photo

interface PhotoSearchRepository {

    suspend fun getPhotos(query: String, page: Int): List<Photo>?
}