package com.example.imfine

import android.app.PendingIntent.getActivity
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.imfine.SplashFind.Companion.Room_ID
import com.example.imfine.SplashFind.Companion.roomExists


class VideoCall : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.videocall)

        val webView = findViewById<WebView>(R.id.webview)

        webView.webViewClient = WebViewClient() // 새 창 띄우기 않기

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

        webView.settings.setSupportZoom(false);  // 줌 설정 여부
        webView.settings.builtInZoomControls = false;  // 줌 확대/축소 버튼 여부

        webView.settings.javaScriptEnabled = true; // 자바스크립트 사용여부
//        webviesdJavascptInterface(new AndroidBridge(), "android");
        webView.settings.javaScriptCanOpenWindowsAutomatically = true; // javascript가 window.open()을 사용할 수 있도록 설정
        webView.settings.setSupportMultipleWindows(true); // 멀티 윈도우 사용 여부

        webView.settings.mediaPlaybackRequiresUserGesture = false



        //webView.settings.domStorageEnabled = true;  // 로컬 스토리지 (localStorage) 사용여부


//        webView.setOnKeyListener(object : DialogInterface.OnKeyListener() {
//            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
//                if (event.getAction() === KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        if (webView != null) {
//                            if (webView.canGoBack()) {
//                                webView.goBack()
//                            } else {
//                                getActivity().onBackPressed()
//                            }
//                        }
//                    }
//                }
//                return true
//            }
//        })


        //웹페이지 호출
//        webView.loadUrl("http://www.naver.com");
        var webUrl = "https://imfine-211027.herokuapp.com/"
        if(roomExists){
            webView.loadUrl(webUrl.plus(Room_ID))
        }
        else{
            webView.loadUrl(webUrl)
        }
        //webView.loadUrl(webUrl.plus(Room_ID))
        //webView.loadUrl(webUrl)
        Log.d("Adad",webUrl.plus(Room_ID))


        //웹뷰 자체에서 전체화면
    }


    override fun onBackPressed() {
        // 뒤로 가기 버튼을 눌렀을때 웹페이지 내에서 뒤로 가기가 있으면 해주고 아니면, 앱 뒤로가기 실행.
        val webView = findViewById<WebView>(R.id.webview)

        // 웹뷰 액티비티 들어가고나서 뒤로가기로 빠져나오면 방(파이어베이스)이 삭제가 안됨
        // (앱을 뒤로가기로 종료도 아니고 완전히 종료해야 사라짐.)
        // 그래서 뒤로가기키 눌렀을때 웹뷰자체를 종료시킴
        // +++되긴되는데 방이 생성되자마자 빠르게 빠져나오면 작동안됨
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            finish()
            webView.clearCache(true)
            webView.destroy()
            Log.d("delete", "backpressed")
        }

    }
}