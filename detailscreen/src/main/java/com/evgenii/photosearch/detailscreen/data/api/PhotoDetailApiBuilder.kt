package com.evgenii.photosearch.detailscreen.data.api

import com.evgenii.photosearch.core.data.api.PhotoApiBuilder
import javax.inject.Inject

class PhotoDetailApiBuilder @Inject constructor() {

    fun buildApi(): PhotoDetailApi =
        PhotoApiBuilder.buildApi().create(PhotoDetailApi::class.java)
}