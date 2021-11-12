package com.example.imfine

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.imfine.SplashFind.Companion.Room_ID
import com.example.imfine.SplashFind.Companion.roomExists
import kotlinx.android.synthetic.main.videocall.*


class VideoCall : AppCompatActivity() {

    private lateinit var webView:WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.videocall)

        val splashFind=Intent(this,SplashFind::class.java)
        webView = findViewById(R.id.webview)

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
            Log.d("roomTest", "Room True")
            webView.loadUrl(webUrl.plus(Room_ID))
            //val bridge = CustomBridge(this,webView)
        //    webView.addJavascriptInterface(bridge,"MyApp")
        }
        else{
            Log.d("roomTest", "Room False")
            webView.loadUrl(webUrl)
            //val bridge = CustomBridge(this,webView)
         //   webView.addJavascriptInterface(bridge,"MyApp")
        }
        //webView.loadUrl(webUrl.plus(Room_ID))
        //webView.loadUrl(webUrl)
        Log.d("Adad",webUrl.plus(Room_ID))
        val bridge = CustomBridge(this,webView)
        webView.addJavascriptInterface(bridge,"MyApp")

        btn_next.setOnClickListener {
            startActivity(splashFind)
            finish()
        }

        btn_exit.setOnClickListener {
            finish()
        }

        //웹뷰 자체에서 전체화면

        val handler = Handler()
        handler.postDelayed(Runnable {
            btn_cameraswitch.visibility = View.VISIBLE
        }, 10000) //딜레이 타임 조절

    }


    override fun onBackPressed() {
        // 뒤로 가기 버튼을 눌렀을때 웹페이지 내에서 뒤로 가기가 있으면 해주고 아니면, 앱 뒤로가기 실행.

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
            Log.d("roomTest", "뒤로가기키 누르고 난 후${Room_ID.toString()}")
        }

    }

    override fun onStop() {
        super.onStop()
        webView.clearCache(true)
        webView.destroy()
        Log.d("roomTest", "onStop${Room_ID.toString()}")
    }
    override fun onDestroy() {
        super.onDestroy()
        webView.clearCache(true)
        webView.destroy()
        Log.d("roomTest", "onDestroy${Room_ID.toString()}")
    }
}