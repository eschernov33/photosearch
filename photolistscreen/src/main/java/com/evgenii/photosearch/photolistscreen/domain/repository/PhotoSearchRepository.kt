package com.evgenii.photosearch.photolistscreen.domain.repository

import androidx.paging.Pager
import com.evgenii.photosearch.core.domain.model.Photo

interface PhotoSearchRepository {

    fun getPhotos(query: String): Pager<Int, Photo>
}