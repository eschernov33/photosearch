package com.evgenii.searchphoto.domain.repository

import retrofit2.Call

interface PhotoSearchRepository<T> {

    fun getPhotos(query: String, page: Int): Call<T>
}