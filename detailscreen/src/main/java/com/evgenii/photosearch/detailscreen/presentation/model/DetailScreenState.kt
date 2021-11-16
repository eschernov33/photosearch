package com.evgenii.photosearch.detailscreen.presentation.model

data class DetailScreenState(
    val photoDetail: PhotoDetailItem? = null,
    val loadState: DetailLoadState = DetailLoadState.Loading,
    val comment: String = ""
) {

    val isErrorVisible: Boolean
        get() = loadState is DetailLoadState.LoadError

    val isLoadingProgressVisible: Boolean
        get() = loadState is DetailLoadState.Loading
}