package com.evgenii.photosearch.photolistscreen.presentation.extensions

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState

fun LoadState.isLoadError(): Boolean =
    this is LoadState.Error

fun CombinedLoadStates.isLoadEmpty(itemCount: Int): Boolean =
    source.refresh is LoadState.NotLoading &&
            append.endOfPaginationReached && itemCount == 0

fun LoadState.isNotLoadingProcess(): Boolean =
    this !is LoadState.Loading
