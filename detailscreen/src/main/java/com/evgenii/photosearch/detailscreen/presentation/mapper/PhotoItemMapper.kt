package com.evgenii.photosearch.detailscreen.presentation.mapper

import com.evgenii.photosearch.core.domain.model.PhotoDetail
import com.evgenii.photosearch.core.presentation.model.PhotoDetailItem
import javax.inject.Inject

class PhotoItemMapper @Inject constructor() {

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