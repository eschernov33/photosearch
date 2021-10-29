package com.evgenii.photosearch.photolistscreen.data.api

import com.evgenii.photosearch.core.data.api.PhotoApiBuilder
import javax.inject.Inject

class PhotoListApiBuilder @Inject constructor() {

    fun buildApi(): PhotoListApi =
        PhotoApiBuilder.buildApi().create(PhotoListApi::class.java)
}