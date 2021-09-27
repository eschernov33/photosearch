package com.evgenii.searchphoto.data.model

class HitsResponseApi(
    val hits: List<HitApi>,
    val total: Int,
    val totalHits: Int,
)