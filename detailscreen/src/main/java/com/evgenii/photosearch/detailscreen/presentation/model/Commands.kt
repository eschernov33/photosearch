package com.evgenii.photosearch.detailscreen.presentation.model

sealed class Commands

class OpenInBrowser(val path: String) : Commands()
object ShowToast : Commands()
object NavigateToBackScreen : Commands()

