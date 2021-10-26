package com.evgenii.photosearch.core.domain.model

class PhotoDetail(
    id: Int,
    user: String,
    userImageURL: String,
    likes: Int,
    downloads: Int,
    largeImageURL: String,
    tags: String,
    val comments: Int,
    val views: Int,
    val pageURL: String
) : Photo(
    id = id,
    user = user,
    userImageURL = userImageURL,
    likes = likes,
    downloads = downloads,
    largeImageURL = largeImageURL,
    tags = tags
)