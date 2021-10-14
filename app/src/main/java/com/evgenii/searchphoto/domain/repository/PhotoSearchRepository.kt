package com.evgenii.searchphoto.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.evgenii.searchphoto.data.source.PhotoListRxPageSource
import com.evgenii.searchphoto.domain.model.Photo

interface PhotoSearchRepository {

    fun getPhotos(
        query: String,
        page: Int,
        pagingSource: PhotoListRxPageSource
    ): LiveData<PagingData<Photo>>

}