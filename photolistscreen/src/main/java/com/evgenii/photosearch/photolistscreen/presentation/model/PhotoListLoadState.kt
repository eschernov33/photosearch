package com.evgenii.photosearch.photolistscreen.presentation.model

sealed class PhotoListLoadState {
    object Loading : PhotoListLoadState()
    object LoadSuccess : PhotoListLoadState()
    object LoadError : PhotoListLoadState()
    object LoadEmpty : PhotoListLoadState()
    object NotLoading : PhotoListLoadState()
}