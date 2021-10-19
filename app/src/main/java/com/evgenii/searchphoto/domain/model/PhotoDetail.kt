package com.evgenii.searchphoto.domain.model

class PhotoDetail(
    val id: Int,
    val user: String,
    val userImageURL: String,
    val likes: Int,
    val downloads: Int,
    val largeImageURL: String,
    val tags: String,
    val comments: Int,
    val views: Int,
    val pageURL: String
)