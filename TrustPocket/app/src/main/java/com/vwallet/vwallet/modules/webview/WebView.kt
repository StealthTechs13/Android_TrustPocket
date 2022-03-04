package com.vwallet.vwallet.modules.webview

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.vwallet.vwallet.R
import kotlinx.android.synthetic.main.activity_webview.*

class WebView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        var textt:String=intent.getStringExtra("texttt").toString()
        var webView = findViewById<android.webkit.WebView>(R.id.webView)
        webView!!.webViewClient =  WebViewClient()
           /* override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }*/
        webView!!.loadUrl(textt)
        webView.settings.javaScriptEnabled=true
        webView.settings.setSupportZoom(true)


    }

    // if you press Back button this code will work
    override fun onBackPressed() {
        // if your webview can go back it will go back
        if (webView.canGoBack())
            webView.goBack()
        // if your webview cannot go back
        // it will exit the application
        else
            super.onBackPressed()
    }
}