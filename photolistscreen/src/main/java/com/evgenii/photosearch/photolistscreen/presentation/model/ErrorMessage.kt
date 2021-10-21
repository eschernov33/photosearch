package com.evgenii.photosearch.photolistscreen.presentation.model

class ErrorMessage(
    val type: Type,
    val message: String? = null
) {
    enum class Type {
        NETWORK,
        NOT_FOUND
    }
}
