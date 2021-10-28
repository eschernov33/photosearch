package com.evgenii.photosearch.detailscreen.presentation.model

class PhotoDetailScreenState(
    val isLoading: Boolean = false,
    val progressBarVisibility: Boolean = false,
    val userName: String = EMPTY,
    val likeCount: String = EMPTY,
    val downloadCount: String = EMPTY,
    val tags: String = EMPTY,
    val commentCount: String = EMPTY,
    val viewsCount: String = EMPTY,
    val userImageURL: String = EMPTY,
    val photoPageUrl: String = EMPTY
) {

    companion object {
        private const val EMPTY = ""
    }
}