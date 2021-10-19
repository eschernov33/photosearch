package com.evgenii.searchphoto.presentation.model

import androidx.paging.PagingData

sealed class PhotosLoadState

class ErrorLoadState(
    val errorMessage: String?
) : PhotosLoadState()

class SuccessLoadState(
    val pagingData: PagingData<PhotoItem>
) : PhotosLoadState()

object OnInitState : PhotosLoadState()
object EmptyLoadState : PhotosLoadState()
object StartLoadingState : PhotosLoadState()
object EndLoadingState : PhotosLoadState()
