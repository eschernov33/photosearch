package com.evgenii.searchphoto.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.evgenii.searchphoto.domain.model.Photo
import com.evgenii.searchphoto.domain.model.PhotoDetail
import io.reactivex.Single

interface PhotoSearchRepository {

    fun getPhotos(query: String): LiveData<PagingData<Photo>>
    fun getPhotoById(photoId: Int): Single<List<PhotoDetail>>
}