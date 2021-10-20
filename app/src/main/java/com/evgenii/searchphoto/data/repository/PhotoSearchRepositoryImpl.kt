package com.evgenii.searchphoto.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.evgenii.searchphoto.data.api.PhotosApi
import com.evgenii.searchphoto.data.mapper.HitApiMapper
import com.evgenii.searchphoto.data.source.PhotoListRxPageSource
import com.evgenii.searchphoto.domain.model.Photo
import com.evgenii.searchphoto.domain.model.PhotoDetail
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import io.reactivex.Single
import javax.inject.Inject

class PhotoSearchRepositoryImpl @Inject constructor(
    private val api: PhotosApi,
    private val mapper: HitApiMapper,
    private val pagingConfig: PagingConfig
) : PhotoSearchRepository {

    override fun getPhotos(
        query: String
    ): LiveData<PagingData<Photo>> {
        val pagingSource = PhotoListRxPageSource(api, query, mapper)
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { pagingSource }
        ).liveData
    }

    override fun getPhotoById(photoId: Int): Single<List<PhotoDetail>> {
        return api.getPhotoById(photoId).map { mapper.mapHitApiResponseToListPhotoDetail(it) }
    }
}