package com.example.imfine

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imfine.SplashFind.Companion.Room_ID
import com.example.imfine.SplashFind.Companion.roomExists
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.rv_chat.*
import kotlinx.android.synthetic.main.videocall.*


class VideoCall : AppCompatActivity() {

    private lateinit var webView:WebView

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatRecyclerViewAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(ChatListViewModel::class.java) }

    val database = Firebase.database
    var myRef: DatabaseReference? = null
    var randomRoom: DatabaseReference? = null

    private var usrName: String? = null
    private var usrEmail: String? = null
    private var usrId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.videocall)

//        전송 버튼을 누르면 상대방 아이디와 채팅한 적있는지 검사한다. : MessageActivity - chekChatRoom()
//        메시지를 보낸다. : MessageActivity - sendMsgToDataBase()
//        리싸이클러뷰 어댑터를 통해 채팅 내용을 읽어들인다.  : RecyclerViewAdapter - getMessageList()

        var btnChatSwitch = true


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

        btn_chat.setOnClickListener {
            if(btnChatSwitch){
                chat_area.visibility = View.VISIBLE
                btnChatSwitch = false
            }else {
                chat_area.visibility = View.GONE
                btnChatSwitch = true
            }
        }

        //웹뷰 자체에서 전체화면

        val handler = Handler()
        handler.postDelayed(Runnable {
            btn_cameraswitch.visibility = View.VISIBLE
        }, 10000) //딜레이 타임 조절

        handler.postDelayed(Runnable {
            //채팅 부분
            adapter = ChatRecyclerViewAdapter(this)
            recyclerView = findViewById(R.id.rv_chat)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter


            if (Room_ID.isNullOrBlank()) {
                myRef = database.getReference(CustomBridge.bridge_ROOM_ID)
            } else {
                myRef = database.getReference(Room_ID!!)
            }
            Log.d("ROOOOOOOMID", Room_ID!!)
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            val rvLayout = layoutInflater.inflate(R.layout.rv_chat, null, false)
            val rvChatArea = rvLayout.findViewById<LinearLayout>(R.id.rv_chat_layout)


            if (acct != null) {
                usrName = acct.displayName
                usrEmail = acct.email
                usrId = acct.id
            }

            btn_chat.visibility = View.VISIBLE

            btn_send.setOnClickListener {

                if (!edittext_sendMsg.text.isNullOrEmpty()) {
                    val msgText = edittext_sendMsg.text
                    Log.d("chatTest", msgText.toString())

                    randomRoom = myRef!!.push()
                    randomRoom!!.child("msg").setValue(msgText.toString())
                    randomRoom!!.child("nickName").setValue(usrName)


                    observerData()
                } else {
                }

                edittext_sendMsg.setText("")
            }

            myRef!!.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    observerData()
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    finish()
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(error: DatabaseError) {}
            })

        }, 5000)

//        //채팅 부분
//        adapter = ChatRecyclerViewAdapter(this)
//        recyclerView = findViewById(R.id.rv_chat)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = adapter
//
//        val database = Firebase.database
//        var myRef: DatabaseReference?
//        if(Room_ID.isNullOrBlank()){
//            myRef = database.getReference("123")
//        }else{
//            myRef = database.getReference(Room_ID!!)
//        }
//        Log.d("ROOOOOOOMID", Room_ID!!)
//        val acct = GoogleSignIn.getLastSignedInAccount(this)
//        val rvLayout = layoutInflater.inflate(R.layout.rv_chat, null, false)
//        val rvChatArea = rvLayout.findViewById<LinearLayout>(R.id.rv_chat_layout)
//
//
//        if (acct != null) {
//            usrName = acct.displayName
//            usrEmail = acct.email
//            usrId = acct.id
//        }
//        btn_send.setOnClickListener {
//
//            if(!edittext_sendMsg.text.isNullOrEmpty()){
//                val msgText = edittext_sendMsg.text
//                Log.d("chatTest", msgText.toString())
//
//                val randomRoom = myRef.push()
//                randomRoom.child("msg").setValue(msgText.toString())
//                randomRoom.child("nickName").setValue(usrName)
//
//
//                observerData()
//            }else{
//            }
//
//            edittext_sendMsg.setText("")
//        }
//
//        myRef.addChildEventListener(object : ChildEventListener{
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                observerData()
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { }
//
//            override fun onChildRemoved(snapshot: DataSnapshot) { }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }
//
//            override fun onCancelled(error: DatabaseError) { }
//        })

    }

    fun observerData(){
        viewModel.fetchData().observe(this, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
            recyclerView.scrollToPosition(it.size -1)
        })
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
            destroyWebviewAndFirebase()
            Log.d("delete", "backpressed")
            Log.d("roomTest", "뒤로가기키 누르고 난 후${Room_ID.toString()}")
        }


    }

//    override fun onBackPressed() {
//        val intent = Intent(this@MessageActivity, MainHome::class.java)
//        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                or Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
//        overridePendingTransition(R.anim.in_left, R.anim.out_right)
//    }


    override fun onStop() {
        super.onStop()
        destroyWebviewAndFirebase()
        Log.d("roomTest", "onStop${Room_ID.toString()}")
    }
    override fun onDestroy() {
        super.onDestroy()
        destroyWebviewAndFirebase()
        Log.d("roomTest", "onDestroy${Room_ID.toString()}")
    }

    override fun onPause() {
        super.onPause()
        destroyWebviewAndFirebase()
    }

    fun destroyWebviewAndFirebase(){
        webView.clearCache(true)
        webView.destroy()
        myRef!!.setValue(null)
    }

}