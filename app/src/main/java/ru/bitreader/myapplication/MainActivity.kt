package ru.bitreader.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    companion object {
        const val TOKEN_PREFS = "TOKEN"
        const val TOKEN_KEY = "csrftoken"
        const val BASE_URL = "https://rt.nikolay.moe"
        const val ADMIN_URL_PART = "/admin/"
    }

    private val webView: WebView by lazy {
        findViewById<WebView>(R.id.web_view)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        CookieManager.getInstance().apply {
            mapOf(TOKEN_KEY to getText()).forEach {
                if (!it.value.isNullOrEmpty()) {
                    setCookie(BASE_URL, "${it.key}=${it.value}")
                }
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settings = webView.settings
        settings.domStorageEnabled = true
        settings.javaScriptEnabled = true
        webView.webViewClient = WebClient().apply {
            newUrlListener = {
                webView.goBack()

                try {
                    val token = getCookie(BASE_URL, TOKEN_KEY).also {
                        it?.let { it1 -> saveText(it1) }
                    }
                }    catch (e: Throwable) {
                }
            }
        }

        webView.loadUrl(BASE_URL + ADMIN_URL_PART)
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun getCookie(siteName: String?, cookieName: String?): String? {
        var cookiesValue: String? = null
        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(siteName)
        val temp = cookies.split(";".toRegex()).toTypedArray()
        for (ar1 in temp) {
            if (ar1.contains(cookieName!!)) {
                val temp1 = ar1.split("=".toRegex()).toTypedArray()
                cookiesValue = temp1[1]
                break
            }
        }
        return cookiesValue
    }


    private fun saveText(text: String) {
        val sPref = getPreferences(MODE_PRIVATE)
        sPref.edit().run {
            putString(TOKEN_PREFS, text)
            apply()
        }
    }

    private fun getText(): String? {
        val sPref = getPreferences(MODE_PRIVATE)
        return sPref.getString(TOKEN_PREFS, null)
    }

}