package com.evgenii.searchphoto.domain.repository

import com.evgenii.searchphoto.data.model.HitsResponseApi
import retrofit2.Call

interface PhotoSearchRepository {

    fun getPhotos(query: String, page: Int): Call<HitsResponseApi>
}