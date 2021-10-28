package com.evgenii.photosearch.core.data.mapper

import com.evgenii.photosearch.core.data.model.PhotoApiItem
import com.evgenii.photosearch.core.data.model.PhotoApiResponse
import com.evgenii.photosearch.core.domain.model.Photo
import com.evgenii.photosearch.core.domain.model.PhotoDetail
import javax.inject.Inject

class PhotoApiMapper @Inject constructor() {

    fun mapPhotoApiResponseToListPhotoDetail(photoApiResponse: PhotoApiResponse)
            : List<PhotoDetail> =
        photoApiResponse.photoApiItems.map(::mapPhotoApiItemToPhotoDetail)

    fun mapPhotoApiResponseToListPhoto(photoApiResponse: PhotoApiResponse): List<Photo> =
        photoApiResponse.photoApiItems.map(::mapPhotoApiItemToPhoto)

    private fun mapPhotoApiItemToPhotoDetail(photoApiItem: PhotoApiItem): PhotoDetail =
        PhotoDetail(
            id = photoApiItem.id,
            user = photoApiItem.user,
            userImageURL = photoApiItem.userImageURL,
            likes = photoApiItem.likes,
            downloads = photoApiItem.downloads,
            largeImageURL = photoApiItem.largeImageURL,
            tags = photoApiItem.tags,
            comments = photoApiItem.comments,
            views = photoApiItem.views,
            pageURL = photoApiItem.pageURL
        )

    private fun mapPhotoApiItemToPhoto(photoApiItem: PhotoApiItem): Photo =
        Photo(
            id = photoApiItem.id,
            user = photoApiItem.user,
            userImageURL = photoApiItem.userImageURL,
            likes = photoApiItem.likes,
            downloads = photoApiItem.downloads,
            largeImageURL = photoApiItem.largeImageURL,
            tags = photoApiItem.tags
        )
}