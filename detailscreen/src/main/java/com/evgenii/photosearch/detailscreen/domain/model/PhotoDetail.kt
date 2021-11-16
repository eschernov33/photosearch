package com.evgenii.photosearch.detailscreen.domain.model

import com.evgenii.photosearch.core.domain.model.Photo

class PhotoDetail(
    id: Int,
    user: String,
    userImageURL: String,
    likes: Int,
    downloads: Int,
    largeImageURL: String,
    tags: String,
    val comments: Int,
    val views: Int
) : Photo(
    id = id,
    user = user,
    userImageURL = userImageURL,
    likes = likes,
    downloads = downloads,
    largeImageURL = largeImageURL,
    tags = tags
)