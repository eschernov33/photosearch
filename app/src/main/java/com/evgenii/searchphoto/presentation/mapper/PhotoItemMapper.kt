package com.evgenii.searchphoto.presentation.mapper

import com.evgenii.searchphoto.domain.model.Photo
import com.evgenii.searchphoto.domain.model.PhotoDetail
import com.evgenii.searchphoto.presentation.model.PhotoDetailItem
import com.evgenii.searchphoto.presentation.model.PhotoItem
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

    fun mapPhotoToPhotoDetailItem(photo: PhotoDetail): PhotoDetailItem =
        PhotoDetailItem(
            photo.id,
            photo.user,
            photo.userImageURL,
            photo.likes.toString(),
            photo.downloads.toString(),
            photo.largeImageURL,
            photo.tags,
            photo.comments.toString(),
            photo.views.toString(),
            photo.pageURL
        )
}