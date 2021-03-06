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
//        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE) //??????????????????
//        ?????? ????????? ????????? ????????? ???????????? ????????? ???????????? ????????????. : MessageActivity - chekChatRoom()
//        ???????????? ?????????. : MessageActivity - sendMsgToDataBase()
//        ?????????????????? ???????????? ?????? ?????? ????????? ???????????????.  : RecyclerViewAdapter - getMessageList()

        var btnChatSwitch = true


        val splashFind=Intent(this,SplashFind::class.java)

        webView = findViewById(R.id.webview)

        webView.webViewClient = WebViewClient() // ??? ??? ????????? ??????

        webView.webChromeClient = object : WebChromeClient() {
            override fun onPermissionRequest(request: PermissionRequest) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    request.grant(request.resources)
                }
            }
        }

        webView.settings.loadWithOverviewMode =
            true  // WebView ??????????????? ???????????? ?????? - setUseWideViewPort ??? ?????? ?????????
        webView.settings.useWideViewPort =
            true  // wide viewport ?????? - setLoadWithOverviewMode ??? ?????? ?????????

        webView.settings.setSupportZoom(false)  // ??? ?????? ??????
        webView.settings.builtInZoomControls = false  // ??? ??????/?????? ?????? ??????

        webView.settings.javaScriptEnabled = true // ?????????????????? ????????????
//        webviesdJavascptInterface(new AndroidBridge(), "android");
        webView.settings.javaScriptCanOpenWindowsAutomatically = true // javascript??? window.open()??? ????????? ??? ????????? ??????
        webView.settings.setSupportMultipleWindows(true) // ?????? ????????? ?????? ??????

        webView.settings.mediaPlaybackRequiresUserGesture = false



        //webView.settings.domStorageEnabled = true;  // ?????? ???????????? (localStorage) ????????????


        //???????????? ??????
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
            destroyWebviewAndFirebase()
        }

        btn_exit.setOnClickListener {
            destroyWebviewAndFirebase()
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

        //?????? ???????????? ????????????

        val handler = Handler()
        handler.postDelayed(Runnable {
            btn_chat.visibility = View.VISIBLE
            btn_siren.visibility = View.VISIBLE
            btn_next.visibility = View.VISIBLE
            btn_exit.visibility = View.VISIBLE
        }, 5000) //????????? ?????? ??????

        //?????? ??????
        handler.postDelayed(Runnable {
            //?????? ??????
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
            imsiRef.child("msg").setValue("${usrName}?????? ?????????????????????!")
            imsiRef.child("nickName").setValue("?????????")


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
                    destroyWebviewAndFirebase()
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
            email.putExtra(Intent.EXTRA_SUBJECT, "${usrName}?????? ?????? ???????????????")
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
        val view = findViewById<ConstraintLayout>(R.id.chat_area) //?????? ??????
        view.isDrawingCacheEnabled = true //????????? ????????? ????????? ???????????? ??????

        //????????? ??????????????? ??????
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
        destroyWebviewAndFirebase()
        Log.d("delete", "backpressed")
        Log.d("roomTest", "??????????????? ????????? ??? ???${Room_ID.toString()}")
    }


//    override fun onBackPressed() {
//        val tempTime = System.currentTimeMillis()
//        val intervalTime: Long = tempTime - backPressedTime
//        if (intervalTime in 0..FINISH_INTERVAL_TIME) {
//            super.onBackPressed()
//            //moveTaskToBack(true)		// ???????????? ?????????????????? ??????
//            // ?????? ?????? ????????? ???????????? ???????????? ????????? ?????? ????????? ????????? ????????? ?????????, ??? ???????????? ??????.
//            if (webView.canGoBack()) {
//                webView.goBack()
//            } else {
//                destroyWebviewAndFirebase()
//                finish()
//                Log.d("delete", "backpressed")
//                Log.d("roomTest", "??????????????? ????????? ??? ???${Room_ID.toString()}")
//            }
//            finishAndRemoveTask()				// ???????????? ?????? + ????????? ??????????????? ?????????
//            android.os.Process.killProcess(android.os.Process.myPid())
//        } else {
//            backPressedTime = tempTime
//            Toast.makeText(this, "?????? ????????? ?????? ??? ???????????? ????????? ???????????????", Toast.LENGTH_SHORT).show()
//        }
//    }

//    override fun onBackPressed() {
//        val intent = Intent(this@MessageActivity, MainHome::class.java)
//        intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                or Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
//        overridePendingTransition(R.anim.in_left, R.anim.out_right)
//    }


//    override fun onDestroy() {
//        super.onDestroy()
//        webView.destroy()
//        destroyWebviewAndFirebase()
//        Log.d("roomTest", "onDestroy${Room_ID.toString()}")
//    }
//
//    override fun onPause() {
//        super.onPause()
//        webView.destroy()
//        destroyWebviewAndFirebase()
//    }


    fun destroyWebviewAndFirebase(){
        // Make sure you remove the WebView from its parent view before doing anything.
        //mWebContainer.removeAllViews();

        webView.clearHistory()

        // NOTE: clears RAM cache, if you pass true, it will also clear the disk cache.
        // Probably not a great idea to pass true if you have other WebViews still alive.
        webView.clearCache(true)

        // Loading a blank page is optional, but will ensure that the WebView isn't doing anything when you destroy it.
        webView.loadUrl("about:blank")

        webView.onPause()
        webView.removeAllViews()
        webView.destroyDrawingCache()

        // NOTE: This pauses JavaScript execution for ALL WebViews,
        // do not use if you have other WebViews still alive.
        // If you create another WebView after calling this,
        // make sure to call mWebView.resumeTimers().
        webView.pauseTimers()

        // NOTE: This can occasionally cause a segfault below API 17 (4.2)
        webView.destroy()

        // Null out the reference so that you don't end up re-using it.
        //webView = null

        if (!myRef!!.key.isNullOrEmpty()) {
            myRef!!.setValue(null)
        }

        finish()
    }

}