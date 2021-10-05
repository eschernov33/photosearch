package com.evgenii.searchphoto.data.model

class HitApiItem(
    val downloads: Int,
    val id: Int,
    val largeImageURL: String,
    val likes: Int,
    val tags: String,
    val user: String,
    val userImageURL: String,
)