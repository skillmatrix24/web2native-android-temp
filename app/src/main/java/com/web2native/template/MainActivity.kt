package com.web2native.template

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.*
import android.net.http.SslError
import android.webkit.SslErrorHandler
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webView = WebView(this)
        setContentView(webView)

        val settings = webView.settings
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
        settings.allowFileAccess = true
        settings.allowContentAccess = true
        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT

        WebView.setWebContentsDebuggingEnabled(true)

        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return false
            }

            override fun onReceivedSslError(
                view: WebView,
                handler: SslErrorHandler,
                error: SslError
            ) {
                // ⚠️ TEMP ONLY – production me remove karenge
                handler.proceed()
            }

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                view.loadData(
                    "<h2>WebView Error</h2><p>${error.description}</p>",
                    "text/html",
                    "UTF-8"
                )
            }
        }

        webView.webChromeClient = WebChromeClient()

        // ✅ REAL TEST
        webView.loadUrl("https://www.google.com")
    }
}
