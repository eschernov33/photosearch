package com.evgenii.photoofday.domain.model

data class PhotoItem(
    val id: Int,
    val user: String,
    val userImageURL: String,
    val likes: Int,
    val downloads: Int,
    val largeImageURL: String,
    val tags: String
)