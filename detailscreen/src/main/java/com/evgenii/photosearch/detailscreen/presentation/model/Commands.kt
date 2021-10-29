package com.evgenii.photosearch.detailscreen.presentation.model

import com.evgenii.photosearch.core.presentation.model.BaseCommands

sealed class Commands : BaseCommands

class OpenInBrowser(val path: String) : Commands()
object ShowToast : Commands()
object NavigateToPrevScreen : Commands()

