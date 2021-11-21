package com.example.imfine

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.imfine.MainPage.Companion.friendVideoCallRoomName
import com.example.imfine.MainPage.Companion.webViewFlag
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_friend_video_call.*

class FriendVideoCall : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_video_call)

        webView = findViewById(R.id.webview_friend)

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

        webView.settings.setSupportZoom(false)  // 줌 설정 여부
        webView.settings.builtInZoomControls = false  // 줌 확대/축소 버튼 여부

        webView.settings.javaScriptEnabled = true // 자바스크립트 사용여부
//        webviesdJavascptInterface(new AndroidBridge(), "android");
        webView.settings.javaScriptCanOpenWindowsAutomatically = true // javascript가 window.open()을 사용할 수 있도록 설정
        webView.settings.setSupportMultipleWindows(true) // 멀티 윈도우 사용 여부

        webView.settings.mediaPlaybackRequiresUserGesture = false



        //webView.settings.domStorageEnabled = true;  // 로컬 스토리지 (localStorage) 사용여부


        //웹페이지 호출
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        val usrName = acct.id
        var webUrl: String? = null
        if(webViewFlag){ //true 일때는 친구아이디(방이름)로 접속
            Log.d("친구한테전화(들어가는사람)", friendVideoCallRoomName.toString())
            webUrl = "https://imfine-211027.herokuapp.com/${friendVideoCallRoomName}"
        }else{
            Log.d("친구한테전화(만든사람)", acct.id.toString())
            webUrl = "https://imfine-211027.herokuapp.com/${acct.id}"
        }
        webView.loadUrl(webUrl)


        btn_exit_friend.setOnClickListener {
            destroyWebviewAndFirebase()
        }

    }


    fun destroyWebviewAndFirebase(){
        webView.clearCache(true)
        webView.destroy()

        val updates = hashMapOf<String, Any>(
            "request" to "disconnected",
            "requestName" to "disconnected"
        )

        val db = Firebase.firestore
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        val callRefFriend = db.collection("Friends").document(acct.email)
        callRefFriend.update(updates)
            .addOnCompleteListener {
                finish()
            }


    }

    override fun onBackPressed() {
        destroyWebviewAndFirebase()
        Log.d("delete", "backpressed")
        Log.d("roomTest", "뒤로가기키 누르고 난 후${SplashFind.Room_ID.toString()}")
    }

}