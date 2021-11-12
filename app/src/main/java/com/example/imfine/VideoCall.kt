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
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imfine.SplashFind.Companion.Room_ID
import com.example.imfine.SplashFind.Companion.roomExists
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.videocall.*
import org.jetbrains.annotations.Nullable


class VideoCall : AppCompatActivity() {

    private lateinit var webView:WebView
    private var mRecyclerView: RecyclerView? = null
    private val mAdapter: RecyclerView.Adapter<*>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null
    private var chatList: List<ChatData>? = null
    private val nick = "nick2" // 1:1 or 1:da로


    private var EditText_chat: EditText? = null
    private var Button_send: Button? = null
    private var myRef: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.videocall)

        Button_send = findViewById(R.id.btn_send)
        EditText_chat = findViewById(R.id.edittext_sendMsg)

        //샌드 눌렀을떄
        //샌드 눌렀을떄
        Button_send!!.setOnClickListener(View.OnClickListener {
            val msg = EditText_chat!!.text.toString() //msg
            //널이 아닐때만 값전송하게
            if (msg != null) {
                val chat = ChatData(nick,msg)  // 오류 났을때 확인해 보기
                chat.nickname = nick
                chat.msg = msg
                myRef!!.push().setValue(chat) //setValue(chat)에서 수정 push() 붙였음
            }
        })

        mRecyclerView = findViewById(R.id.rv_chat);
        mRecyclerView!!.setHasFixedSize(true);
        mLayoutManager = LinearLayoutManager(this);
        mRecyclerView!!.layoutManager = mLayoutManager;

        chatList = ArrayList()
        //어뎁터

        // Write a message to the database
        //database 선언과 생성
        // Write a message to the database
        //database 선언과 생성
        val database = FirebaseDatabase.getInstance()
        //message를 참조(getReference)해서 가져옴.
        //message를 참조(getReference)해서 가져옴.
        myRef = database.reference

        /* ChatData chat = new ChatData();
        chat.setNickname(nick);
        chat.setMsg("hi");
        myRef.setValue(chat);*/
        //주의사항


        /* ChatData chat = new ChatData();
        chat.setNickname(nick);
        chat.setMsg("hi");
        myRef.setValue(chat);*/
        //주의사항
        myRef!!.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, @Nullable s: String?) {}
            override fun onChildChanged(dataSnapshot: DataSnapshot, @Nullable s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, @Nullable s: String?) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
        //0.채팅 앱 만들기
        //1. recycleView - 어떤 데이터를 반복해 보여주는 용도로 많이씀.
        //2. 데이터베이서 내용을 넣는다.
        //3. 상대방폰에 채팅 내용이 보이게.

        //1-1. recycleview - chat data
        //1. message, nickname, ismine - Data Transfer Object(데이터를 교환하는 객체


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

        webView.settings.setSupportZoom(false)  // 줌 설정 여부
        webView.settings.builtInZoomControls = false  // 줌 확대/축소 버튼 여부

        webView.settings.javaScriptEnabled = true // 자바스크립트 사용여부
//        webviesdJavascptInterface(new AndroidBridge(), "android");
        webView.settings.javaScriptCanOpenWindowsAutomatically = true // javascript가 window.open()을 사용할 수 있도록 설정
        webView.settings.setSupportMultipleWindows(true) // 멀티 윈도우 사용 여부

        webView.settings.mediaPlaybackRequiresUserGesture = false



        //webView.settings.domStorageEnabled = true;  // 로컬 스토리지 (localStorage) 사용여부


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