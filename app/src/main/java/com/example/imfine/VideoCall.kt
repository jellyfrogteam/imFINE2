package com.example.imfine

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.imfine.SplashFind.Companion.Room_ID
import com.example.imfine.SplashFind.Companion.roomExists
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.videocall.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.NullPointerException


class VideoCall : AppCompatActivity() {
    private val FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0

    private lateinit var webView:WebView

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatRecyclerViewAdapter
    private val viewModel by lazy { ViewModelProvider(this).get(ChatListViewModel::class.java) }

    val database = Firebase.database
    var myRef: DatabaseReference? = null
    var randomRoom: DatabaseReference? = null
    var myRefFire = Firebase.firestore

    private var usrName: String? = null
    private var usrEmail: String? = null
    private var usrId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.videocall)
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
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
            //onDestroy()
            //webView.onPause()
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
            btn_chat.visibility = View.VISIBLE
            btn_siren.visibility = View.VISIBLE
            btn_next.visibility = View.VISIBLE
            btn_exit.visibility = View.VISIBLE
        }, 5000) //딜레이 타임 조절

        //채팅 부분
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

            val acct = GoogleSignIn.getLastSignedInAccount(this)
            val rvLayout = layoutInflater.inflate(R.layout.rv_chat, null, false)
            val rvChatArea = rvLayout.findViewById<LinearLayout>(R.id.rv_chat_layout)


            if (acct != null) {
                usrName = acct.displayName
                usrEmail = acct.email
                usrId = acct.id
            }


            val imsiRef = myRef!!.push()
            imsiRef.child("msg").setValue("${usrName}님이 입장하셨습니다!")
            imsiRef.child("nickName").setValue("관리자")


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

        val view = findViewById<View>(R.id.rootView)
        btn_siren.setOnClickListener{
            ScreenShot()
            /*val cap = ScreenShotActivity(view)
            val fileUri: Uri = Uri.fromFile(cap)

            val email = Intent(Intent.ACTION_SEND)
            email.type = "plain/text"
            val address = arrayOf("jellyfrogteam@gmail.com")
            email.putExtra(Intent.EXTRA_EMAIL, address)
            email.putExtra(Intent.EXTRA_SUBJECT, "${usrName}님의 신고 내용입니다")
            email.putExtra(Intent.EXTRA_STREAM, cap)
            Log.d("capaaaaaa", cap.toString())
            startActivity(email)*/
        }

//        if (Room_ID.isNullOrBlank()) {
//            myRef = database.getReference(CustomBridge.bridge_ROOM_ID)
//        } else {
//            val docRef = myRefFire.collection("rooms")
//                .document(Room_ID!!).get()
//            if(docRef == null) {  }
//        }


    }

    fun observerData(){
        viewModel.fetchData().observe(this, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
            recyclerView.scrollToPosition(it.size -1)
        })
    }

    fun ScreenShot() {
//        val view = window.decorView.rootView
        val view = findViewById<ConstraintLayout>(R.id.chat_area) //채팅 구역
        view.isDrawingCacheEnabled = true //화면에 뿌릴때 캐시를 사용하게 한다

        //캐시를 비트맵으로 변환
        val screenBitmap = Bitmap.createBitmap(view.drawingCache)
        try {
            val cachePath = File(applicationContext.cacheDir, "images")
            cachePath.mkdirs() // don't forget to make the directory
            val stream =
                FileOutputStream("$cachePath/image.png") // overwrites this image every time
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
            val newFile = File(cachePath, "image.png")
            val contentUri = FileProvider.getUriForFile(
                applicationContext,
                "com.example.imfine", newFile
            )
            val Sharing_intent = Intent(Intent.ACTION_SEND)
            Sharing_intent.type = "image/png"
            Sharing_intent.putExtra(Intent.EXTRA_STREAM, contentUri)
            startActivity(Intent.createChooser(Sharing_intent, "Share image"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            destroyWebviewAndFirebase()
            finish()
            Log.d("delete", "backpressed")
            Log.d("roomTest", "뒤로가기키 누르고 난 후${Room_ID.toString()}")
        }
    }


//    override fun onBackPressed() {
//        val tempTime = System.currentTimeMillis()
//        val intervalTime: Long = tempTime - backPressedTime
//        if (intervalTime in 0..FINISH_INTERVAL_TIME) {
//            super.onBackPressed()
//            //moveTaskToBack(true)		// 태스크를 백그라운드로 이동
//            // 뒤로 가기 버튼을 눌렀을때 웹페이지 내에서 뒤로 가기가 있으면 해주고 아니면, 앱 뒤로가기 실행.
//            if (webView.canGoBack()) {
//                webView.goBack()
//            } else {
//                destroyWebviewAndFirebase()
//                finish()
//                Log.d("delete", "backpressed")
//                Log.d("roomTest", "뒤로가기키 누르고 난 후${Room_ID.toString()}")
//            }
//            finishAndRemoveTask()				// 액티비티 종료 + 태스크 리스트에서 지우기
//            android.os.Process.killProcess(android.os.Process.myPid())
//        } else {
//            backPressedTime = tempTime
//            Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 통화가 종료됩니다", Toast.LENGTH_SHORT).show()
//        }
//    }

//    override fun onBackPressed() {
//        val intent = Intent(this@MessageActivity, MainHome::class.java)
//        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                or Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
//        overridePendingTransition(R.anim.in_left, R.anim.out_right)
//    }


    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
        destroyWebviewAndFirebase()
        Log.d("roomTest", "onDestroy${Room_ID.toString()}")
    }

    override fun onPause() {
        super.onPause()
        webView.destroy()
        destroyWebviewAndFirebase()
    }


    fun destroyWebviewAndFirebase(){
        webView.clearCache(true)
        webView.destroy()

        if (!myRef!!.key.isNullOrEmpty()) {
            myRef!!.setValue(null)
        }

    }

}