package com.evgenii.searchphoto.data.model

data class HitsResponseApi(
    val hits: List<HitApi>,
    val total: Int,
    val totalHits: Int
)