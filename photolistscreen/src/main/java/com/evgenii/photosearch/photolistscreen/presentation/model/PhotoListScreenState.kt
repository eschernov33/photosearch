package com.evgenii.photosearch.photolistscreen.presentation.model

import androidx.annotation.StringRes
import com.evgenii.photosearch.core.presentation.model.BaseScreenState
import com.evgenii.photosearch.photolistscreen.R

class PhotoListScreenState(
    val isPhotoListVisible: Boolean = false,
    val isLoadingProgressBarVisible: Boolean = false,
    val isErrorTextVisible: Boolean = false,
    @StringRes val errorTextResId: Int = R.string.empty,
    val isRetryButtonVisible: Boolean = false,
    val isContentBlockVisible: Boolean = isPhotoListVisible || isErrorTextVisible
) : BaseScreenState
