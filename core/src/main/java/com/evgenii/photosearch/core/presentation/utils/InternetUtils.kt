package com.evgenii.photosearch.core.presentation.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

class InternetUtils {

    companion object {
        fun openInBrowser(context: Context, url: String) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            context.startActivity(intent)
        }
    }
}