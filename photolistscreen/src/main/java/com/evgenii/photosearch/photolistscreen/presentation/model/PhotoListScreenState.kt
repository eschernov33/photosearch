package com.evgenii.photosearch.photolistscreen.presentation.model

class PhotoListScreenState(
    val photoListVisibility: Boolean = false,
    val loadingProgressBarVisibility: Boolean = false,
    val errorTextViewVisibility: Boolean = false,
    val errorType: ErrorType? = null,
    val retryButtonVisibility: Boolean = errorType == ErrorType.NETWORK,
    val contentBlockVisibility: Boolean = photoListVisibility || errorTextViewVisibility
)
