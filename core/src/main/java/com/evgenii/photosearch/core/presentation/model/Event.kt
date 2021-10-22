package com.evgenii.photosearch.core.presentation.model

class Event<T>(
    private val value: T
) {

    private var handled: Boolean = false

    fun getValue(): T? {
        if (handled) {
            return null
        }
        handled = true
        return value
    }
}