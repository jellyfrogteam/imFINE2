package com.example.imfine

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class HomeFragment : Fragment() {
    private lateinit var webView2: WebView
    private val webUrl = "file:///android_asset/localVideo.html"

    companion object{
        lateinit var btn_Convert: ImageButton
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        val context = container?.context
        // Inflate the layout for this fragment

        val oneOne:ImageButton = view.findViewById(R.id.oneone)
        val everyOne:ImageButton = view.findViewById(R.id.everyone)
        btn_Convert = view.findViewById(R.id.btn_convert)


        webView2 = view.findViewById(R.id.webview_preview)

        webView2.webViewClient = WebViewClient() // 새 창 띄우기 않기

        webView2.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    request.grant(request.resources)
                }
            }

        }

        webView2.settings.loadWithOverviewMode =
            true  // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
        webView2.settings.useWideViewPort =
            true  // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함

        webView2.settings.setSupportZoom(false);  // 줌 설정 여부
        webView2.settings.builtInZoomControls = false;  // 줌 확대/축소 버튼 여부

        webView2.settings.javaScriptEnabled = true; // 자바스크립트 사용여부
//        webviesdJavascptInterface(new AndroidBridge(), "android");
        webView2.settings.javaScriptCanOpenWindowsAutomatically = true; // javascript가 window.open()을 사용할 수 있도록 설정
        webView2.settings.setSupportMultipleWindows(true); // 멀티 윈도우 사용 여부

        webView2.settings.mediaPlaybackRequiresUserGesture = false

        //웹페이지 호출
        webView2.loadUrl(webUrl)


    /*   val bridge = CustomBridgePreview(context,webView)
        webView.addJavascriptInterface(bridge,"MyApp")*/


        oneOne.setOnClickListener {
            val splashFind = Intent(activity, SplashFind::class.java)
            startActivity(splashFind)

        }

        btn_Convert.setOnClickListener {

        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        webView2.destroy()
    }

    override fun onPause() {
        super.onPause()
        webView2.destroy()
    }

    override fun onResume() {
        super.onResume()
        Log.d("FragmentView", webView2.toString())
        Log.d("FragmentView", "onResume")
        Log.d("FragmentView", webUrl)
        webView2.loadUrl(webUrl)
    }


}