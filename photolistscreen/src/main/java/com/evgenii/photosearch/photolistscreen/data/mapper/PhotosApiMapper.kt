package com.evgenii.photosearch.photolistscreen.data.mapper

import com.evgenii.photosearch.core.data.model.PhotoApiItem
import com.evgenii.photosearch.core.data.model.PhotoApiResponse
import com.evgenii.photosearch.core.domain.model.Photo
import javax.inject.Inject

class PhotosApiMapper @Inject constructor() {

    fun mapPhotoApiResponseToListPhoto(photoApiResponse: PhotoApiResponse): List<Photo> =
        photoApiResponse.photoApiItems.map(::mapPhotoApiItemToPhoto)

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