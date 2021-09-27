package com.evgenii.searchphoto.data.model

import com.squareup.moshi.Json

class HitApi(
    val collections: Int,
    val comments: Int,
    val downloads: Int,
    val id: Int,
    val imageHeight: Int,
    val imageSize: Int,
    val imageWidth: Int,
    val largeImageURL: String,
    val likes: Int,
    val pageURL: String,
    val previewHeight: Int,
    val previewURL: String,
    val previewWidth: Int,
    val tags: String,
    val type: String,
    val user: String,
    val userImageURL: String,
    @Json(name = "user_id")
    val userId: Int,
    val views: Int,
    @Json(name = "webformatHeight")
    val webFormatHeight: Int,
    @Json(name = "webformatURL")
    val webFormatURL: String,
    @Json(name = "webformatWidth")
    val webFormatWidth: Int,
)