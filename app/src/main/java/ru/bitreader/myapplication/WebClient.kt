package ru.bitreader.myapplication

import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class WebClient : WebViewClient() {

    var newUrlListener: (WebResourceRequest?) -> Unit = {}

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        Log.d("bzz", request?.url.toString())
        newUrlListener.invoke(request)
        return super.shouldOverrideUrlLoading(view, request)
    }

}