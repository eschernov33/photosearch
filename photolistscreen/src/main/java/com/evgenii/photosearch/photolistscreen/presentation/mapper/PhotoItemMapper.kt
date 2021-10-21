package com.evgenii.photosearch.photolistscreen.presentation.mapper

import com.evgenii.core.domain.model.Photo
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoItem
import javax.inject.Inject

class PhotoItemMapper @Inject constructor() {

    fun mapPhotoToPhotoItem(photo: Photo): PhotoItem =
        PhotoItem(
            photo.id,
            photo.user,
            photo.userImageURL,
            photo.likes.toString(),
            photo.downloads.toString(),
            photo.largeImageURL,
            photo.tags
        )
}