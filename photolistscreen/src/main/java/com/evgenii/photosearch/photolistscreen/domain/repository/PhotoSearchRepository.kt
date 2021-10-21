package com.evgenii.photosearch.photolistscreen.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.evgenii.core.domain.model.Photo

interface PhotoSearchRepository {

    fun getPhotos(query: String): LiveData<PagingData<Photo>>
}