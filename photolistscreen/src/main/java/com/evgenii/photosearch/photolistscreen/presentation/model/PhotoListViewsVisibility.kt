package com.evgenii.photosearch.photolistscreen.presentation.model

class PhotoListViewsVisibility(
    val listVisible: Boolean,
    val progressPhotoLoadVisible: Boolean,
    val errorMessageVisible: Boolean,
    val btnRetryVisible: Boolean,
    val areaVisible: Boolean = listVisible || errorMessageVisible,
)