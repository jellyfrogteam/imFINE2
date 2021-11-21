package com.example.imfine

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton

class OneToMany : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var exit : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_to_many)


        exit = findViewById(R.id.btn_exit_otm)
        webView = findViewById(R.id.webview_otm)
        webView.webViewClient = WebViewClient()

        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    request.grant(request.resources)
                }
            }
        }

        webView.settings.loadWithOverviewMode =
            true  // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
        webView.settings.useWideViewPort =
            true  // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함

        webView.settings.setSupportZoom(false)  // 줌 설정 여부
        webView.settings.builtInZoomControls = false  // 줌 확대/축소 버튼 여부

        webView.settings.javaScriptEnabled = true // 자바스크립트 사용여부
//        webviesdJavascptInterface(new AndroidBridge(), "android");
        webView.settings.javaScriptCanOpenWindowsAutomatically = true // javascript가 window.open()을 사용할 수 있도록 설정
        webView.settings.setSupportMultipleWindows(true) // 멀티 윈도우 사용 여부

        webView.settings.mediaPlaybackRequiresUserGesture = false

        var webUrl = "https://imfine-211108-nn.herokuapp.com/aaa"
//        if(SplashFind_otm.roomExists_otm){
//            Log.d("roomTest", "Room True")
//            webView.loadUrl(webUrl.plus(SplashFind_otm.Room_ID_otm))
//            //val bridge = CustomBridge(this,webView)
//            //    webView.addJavascriptInterface(bridge,"MyApp")
//        }
//        else{
//            Log.d("roomTest", "Room False")
//            webView.loadUrl(webUrl)
//            //val bridge = CustomBridge(this,webView)
//            //   webView.addJavascriptInterface(bridge,"MyApp")
//        }
        webView.loadUrl(webUrl)
        //webView.loadUrl(webUrl.plus(Room_ID))
        //webView.loadUrl(webUrl)
        Log.d("Adad",webUrl.plus(SplashFind_otm.Room_ID_otm))





        exit.setOnClickListener {
            destroyWebviewAndFirebase()
        }
    }

    fun destroyWebviewAndFirebase(){
        webView.clearCache(true)
        webView.destroy()

        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        destroyWebviewAndFirebase()
    }
}