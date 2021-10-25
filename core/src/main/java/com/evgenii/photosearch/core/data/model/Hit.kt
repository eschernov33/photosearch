package com.evgenii.photosearch.core.data.model

class Hit(
    val downloads: Int,
    val id: Int,
    val largeImageURL: String,
    val likes: Int,
    val tags: String,
    val user: String,
    val userImageURL: String,
    val comments: Int,
    val views: Int,
    val pageURL: String
)