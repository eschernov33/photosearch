package com.evgenii.photosearch.photolistscreen.presentation.model

sealed class Commands

class ShowDetail(val photoItem: PhotoItem) : Commands()
object HideKeyboard : Commands()
