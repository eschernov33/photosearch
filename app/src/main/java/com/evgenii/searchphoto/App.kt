package com.evgenii.searchphoto

import android.app.Application
import com.evgenii.searchphoto.data.repository.PhotoSearchRepositoryImpl
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository

class App : Application() {

    val photoSearchRepository: PhotoSearchRepository = PhotoSearchRepositoryImpl()
}