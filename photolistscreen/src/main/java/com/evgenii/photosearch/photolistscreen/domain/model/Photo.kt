package com.evgenii.photosearch.photolistscreen.domain.model

class Photo(
    val id: Int,
    val user: String,
    val userImageURL: String,
    val likes: Int,
    val downloads: Int,
    val largeImageURL: String,
    val tags: String,
)