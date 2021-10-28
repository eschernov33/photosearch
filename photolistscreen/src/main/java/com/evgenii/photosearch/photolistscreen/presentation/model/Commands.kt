package com.evgenii.photosearch.photolistscreen.presentation.model

import com.evgenii.photosearch.core.presentation.model.BaseCommands

sealed class Commands : BaseCommands

class ShowDetail(val photoItem: PhotoItem) : Commands()
object HideKeyboard : Commands()
