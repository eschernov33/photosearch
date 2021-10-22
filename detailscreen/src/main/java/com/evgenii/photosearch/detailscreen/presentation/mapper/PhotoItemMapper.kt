package com.evgenii.photosearch.detailscreen.presentation.mapper

import com.evgenii.photosearch.core.domain.model.PhotoDetail
import com.evgenii.photosearch.core.presentation.model.PhotoDetailItem
import javax.inject.Inject

class PhotoItemMapper @Inject constructor() {

    fun mapPhotoToPhotoDetailItem(photo: PhotoDetail): PhotoDetailItem =
        PhotoDetailItem(
            id = photo.id,
            user = photo.user,
            userImageURL = photo.userImageURL,
            likes = photo.likes.toString(),
            downloads = photo.downloads.toString(),
            largeImageURL = photo.largeImageURL,
            tags = photo.tags,
            comments = photo.comments.toString(),
            views = photo.views.toString(),
            pageURL = photo.pageURL
        )
}