package com.evgenii.searchphoto.domain.repository

import com.evgenii.searchphoto.data.model.HitApiList
import retrofit2.Call

interface PhotoSearchRepository {

    fun getPhotos(query: String, page: Int): Call<HitApiList>
}