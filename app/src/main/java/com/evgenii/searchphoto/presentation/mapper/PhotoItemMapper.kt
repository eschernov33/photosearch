package com.evgenii.searchphoto.presentation.mapper

import com.evgenii.searchphoto.domain.model.Photo
import com.evgenii.searchphoto.presentation.model.PhotoItem

class PhotoItemMapper {

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