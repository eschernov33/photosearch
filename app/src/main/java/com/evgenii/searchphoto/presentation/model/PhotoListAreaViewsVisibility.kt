package com.evgenii.searchphoto.presentation.model

class PhotoListAreaViewsVisibility(
    val listVisible: Boolean,
    val progressPhotoLoadVisible: Boolean,
    val errorMessageVisible: Boolean,
    val areaVisible: Boolean = listVisible || errorMessageVisible
)