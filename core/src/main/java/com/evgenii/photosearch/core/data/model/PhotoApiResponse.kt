package com.evgenii.photosearch.core.data.model

import com.squareup.moshi.Json

class PhotoApiResponse(
    @Json(name = "hits")
    val photoApiItems: List<PhotoApiItem>
)