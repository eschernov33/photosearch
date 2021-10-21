package com.evgenii.photosearch.core.presentation.utils

import android.annotation.SuppressLint
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Suppress("unused")
@SuppressLint("CustomX509TrustManager")
class HttpsTrustManager : X509TrustManager {

    private val acceptedIssuers = arrayOf<X509Certificate>()

    override fun checkClientTrusted(x509Certificates: Array<out X509Certificate>?, s: String?) =
        Unit

    override fun checkServerTrusted(x509Certificates: Array<out X509Certificate>?, s: String?) =
        Unit

    override fun getAcceptedIssuers(): Array<X509Certificate> = acceptedIssuers

    companion object {
        private var trustManagers: Array<TrustManager>? = null

        fun allowAllSSL() {
            HttpsURLConnection.setDefaultHostnameVerifier { _, _ -> true }
            var context: SSLContext? = null
            if (trustManagers == null) {
                trustManagers = arrayOf(HttpsTrustManager())
            }
            try {
                context = SSLContext.getInstance("TLS")
                context.init(null, trustManagers, SecureRandom())
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            } catch (e: KeyManagementException) {
                e.printStackTrace()
            }
            HttpsURLConnection.setDefaultSSLSocketFactory(
                context?.socketFactory
            )
        }
    }
}