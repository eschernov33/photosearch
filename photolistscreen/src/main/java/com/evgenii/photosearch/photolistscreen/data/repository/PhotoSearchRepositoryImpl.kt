package com.evgenii.photosearch.photolistscreen.data.repository

import androidx.paging.PagingConfig
import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.mapper.HitApiMapper
import com.evgenii.photosearch.core.domain.model.Photo
import com.evgenii.photosearch.photolistscreen.domain.repository.PhotoSearchRepository
import javax.inject.Inject

class PhotoSearchRepositoryImpl @Inject constructor(
    private val api: PhotosApi,
    private val mapper: HitApiMapper,
    private val pagingConfig: PagingConfig
) : PhotoSearchRepository {

//    override suspend fun getPhotos(
//        query: String
//    ): Pager<Int, Photo> {
//        val pagingSource = PhotoListPageSource(api, query, mapper)
//        return Pager(
//            config = pagingConfig,
//            pagingSourceFactory = { pagingSource }
//        )
//    }

    override suspend fun getPhotos(query: String, page: Int): List<Photo>? {
        val response = api.getPhotos(query, page).body() ?: return null
        return mapper.mapHitApiResponseToListPhoto(response)
    }
}