package com.evgenii.photosearch.photolistscreen.presentation.mapper

import com.evgenii.photosearch.core.domain.model.Photo
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoItem
import javax.inject.Inject

class PhotoItemMapper @Inject constructor() {

    fun mapPhotoToPhotoItem(photo: Photo): PhotoItem =
        PhotoItem(
            id = photo.id,
            user = photo.user,
            userImageURL = photo.userImageURL,
            likes = photo.likes.toString(),
            downloads = photo.downloads.toString(),
            largeImageURL = photo.largeImageURL,
            tags = photo.tags
        )
}