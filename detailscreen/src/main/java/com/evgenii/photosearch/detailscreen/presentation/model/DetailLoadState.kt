package com.evgenii.photosearch.detailscreen.presentation.model

sealed class DetailLoadState {

    object Loading : DetailLoadState()
    object LoadError : DetailLoadState()
    object LoadSuccess : DetailLoadState()
}