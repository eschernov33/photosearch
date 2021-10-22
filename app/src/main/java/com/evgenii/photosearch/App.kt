package com.evgenii.photosearch

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    /** Add [HttpsTrustManager.allowAllSSL()] to solve the problem with certificates
     * @see HttpsTrustManager
     */
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}