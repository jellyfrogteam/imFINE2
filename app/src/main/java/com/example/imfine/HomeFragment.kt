package com.example.imfine

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    companion object{

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        checkPermission()
        webview2.webViewClient = WebViewClient() // 새 창 띄우기 않기

        webview2.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    request.grant(request.resources)
                }
            }
        }

        webview2.settings.loadWithOverviewMode =
            true  // WebView 화면크기에 맞추도록 설정 - setUseWideViewPort 와 같이 써야함
        webview2.settings.useWideViewPort =
            true  // wide viewport 설정 - setLoadWithOverviewMode 와 같이 써야함

        webview2.settings.setSupportZoom(false);  // 줌 설정 여부
        webview2.settings.builtInZoomControls = false;  // 줌 확대/축소 버튼 여부

        webview2.settings.javaScriptEnabled = true; // 자바스크립트 사용여부
//        webviesdJavascptInterface(new AndroidBridge(), "android");
        webview2.settings.javaScriptCanOpenWindowsAutomatically = true; // javascript가 window.open()을 사용할 수 있도록 설정
        webview2.settings.setSupportMultipleWindows(true); // 멀티 윈도우 사용 여부

        webview2.settings.mediaPlaybackRequiresUserGesture = false



        //webView.settings.domStorageEnabled = true;  // 로컬 스토리지 (localStorage) 사용여부


        //웹페이지 호출
//        webView.loadUrl("http://www.naver.com");
        var webUrl = "https://imfine-211027.herokuapp.com/"
        webview2.loadUrl(webUrl.plus(MainActivity.Room_ID))
        Log.d("Adad",webUrl.plus(MainActivity.Room_ID))
        //웹뷰 자체에서 전체화면

        return inflater.inflate(R.layout.fragment_home, container, false)


        // (1) View로 변환
        //val view:View = inflater.inflate(R.layout.fragment_menu1, container, false)

        // (2) View를 사용해 findViewById 메서드 사용
        //val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView1)

    }

    fun checkPermission() {

        val permissions = arrayOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE
        )

        for(i in permissions){
            val per = activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    i
                )
            }
            if (per != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없는 경우 permission 권한을 띄우는 알람창을 띄운다.
                activity?.let {
                    ActivityCompat.requestPermissions(
                        it,
                        permissions,
                        1000
                    )
                }
            } else {
                // 권한이 있는 경우
                // Toast.makeText(this, "카메라,audio를 실행합니다", Toast.LENGTH_LONG).show()
            }
        }

    }

    //권한을 두는 팝업창에 사용자가 Deny Eh또는 수락을 클릭하면 액태비티의 onRequestPermissionResult()가 호출된다. 이 메서드 안에서 승인 후 처리하면 된다.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Toast 창 띄우기
                Toast.makeText(activity, "카메라,audio를 실행합니다", Toast.LENGTH_LONG).show()
            } else {
                // 액티비티 끝내기(이전페이지)
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.remove(this)
                    ?.commit()
            }
        }
    }
    
}