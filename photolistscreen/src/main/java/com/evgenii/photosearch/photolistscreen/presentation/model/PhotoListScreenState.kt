package com.evgenii.photosearch.photolistscreen.presentation.model

import androidx.annotation.StringRes
import androidx.paging.PagingData
import com.evgenii.photosearch.photolistscreen.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class PhotoListScreenState(
    val photoList: Flow<PagingData<PhotoItem>> = flow { },
    val loadState: PhotoListLoadState = PhotoListLoadState.NotLoading,
    @StringRes val errorMessage: Int = R.string.empty
) {
    val isListVisible: Boolean
        get() = loadState is PhotoListLoadState.LoadSuccess

    val isErrorVisible: Boolean
        get() = loadState is PhotoListLoadState.LoadError || loadState is PhotoListLoadState.LoadEmpty

    val isRetryButtonVisible: Boolean
        get() = loadState is PhotoListLoadState.LoadError

    val isSearchLoadingProgressVisible: Boolean
        get() = loadState == PhotoListLoadState.Loading
}