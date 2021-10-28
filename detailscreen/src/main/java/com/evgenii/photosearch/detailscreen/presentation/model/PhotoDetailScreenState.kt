package com.evgenii.photosearch.detailscreen.presentation.model

import com.evgenii.photosearch.core.presentation.model.BaseScreenState

class PhotoDetailScreenState(
    val isLoading: Boolean = false,
    val progressBarVisibility: Boolean = false,
    val userName: String = "",
    val likeCount: String = "",
    val downloadCount: String = "",
    val tags: String = "",
    val commentCount: String = "",
    val viewsCount: String = "",
    val userImageURL: String = "",
    val photoPageUrl: String = ""
) : BaseScreenState