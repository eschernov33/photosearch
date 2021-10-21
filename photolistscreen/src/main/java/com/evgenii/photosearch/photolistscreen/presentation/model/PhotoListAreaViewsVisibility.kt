package com.evgenii.photosearch.photolistscreen.presentation.model

class PhotoListAreaViewsVisibility(
    val listVisible: Boolean,
    val progressPhotoLoadVisible: Boolean,
    val errorMessageVisible: Boolean,
    val areaVisible: Boolean = listVisible || errorMessageVisible
)