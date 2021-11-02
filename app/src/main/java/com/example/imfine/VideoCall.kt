package com.example.imfine

import android.os.Bundle
import android.webkit.DownloadListener
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity


class VideoCall : AppCompatActivity() {
    private var webview  : WebView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.videocall)

        val webView = findViewById<WebView>(R.id.webview)


        webView.webViewClient = WebViewClient() // 새 창 띄우기 않기

        webView.webChromeClient = WebChromeClient()

        webView.settings.loadWithOverviewMode = true;  // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
        webView.settings.useWideViewPort = true;  // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함

        webView.settings.setSupportZoom(false);  // 줌 설정 여부
        webView.settings.builtInZoomControls = false;  // 줌 확대/축소 버튼 여부

        webView.settings.javaScriptEnabled = true; // 자바스크립트 사용여부
//        webviesdJavascptInterface(new AndroidBridge(), "android");
        webView.settings.javaScriptCanOpenWindowsAutomatically = true; // javascript가 window.open()을 사용할 수 있도록 설정
        webView.settings.setSupportMultipleWindows(true); // 멀티 윈도우 사용 여부

        //webView.settings.domStorageEnabled = true;  // 로컬 스토리지 (localStorage) 사용여부


        //웹페이지 호출
//        webView.loadUrl("http://www.naver.com");
        webView.loadUrl("https://imfine-211027.herokuapp.com/");
    }
}