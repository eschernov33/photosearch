package com.evgenii.photosearch.detailscreen.data.mapper

import com.evgenii.photosearch.core.data.model.PhotoApiItem
import com.evgenii.photosearch.core.data.model.PhotoApiResponse
import com.evgenii.photosearch.detailscreen.domain.model.PhotoDetail
import javax.inject.Inject

class PhotoDetailApiMapper @Inject constructor() {

    fun mapPhotoApiResponseToListPhotoDetail(photoApiResponse: PhotoApiResponse)
            : List<PhotoDetail> =
        photoApiResponse.photoApiItems.map(::mapPhotoApiItemToPhotoDetail)

    fun mapPhotoApiItemToPhotoDetail(photoApiItem: PhotoApiItem): PhotoDetail =
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
        )
}