package com.evgenii.photosearch.photolistscreen.presentation.model

class UserSearch {
    var isAlreadySearch: Boolean = false

    var query: String = ""
        set(value) {
            isAlreadySearch = true
            field = value
        }
}