package com.evgenii.photosearch.photolistscreen.domain.repository

import androidx.paging.PagingData
import com.evgenii.photosearch.core.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoSearchRepository {

    fun getPhotos(query: String): Flow<PagingData<Photo>>
}