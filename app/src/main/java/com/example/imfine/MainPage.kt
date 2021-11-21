package com.example.imfine

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.google.firestore.v1.DocumentChange
import kotlinx.android.synthetic.main.main.*


class MainPage : AppCompatActivity(){

    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var db:FirebaseFirestore
    private lateinit var acct:GoogleSignInAccount
    private lateinit var callRef:DocumentReference
    private lateinit var friendCallName: TextView
    private lateinit var friendCallLayout: LinearLayout
    private lateinit var btn_callAccept: ImageButton
    private lateinit var btn_callDeny: ImageButton


    companion object{
        var friendVideoCallRoomName: String? = null //나중에 안전하게 해주기
        var webViewFlag = true //나중에 안전하게 해주기
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        db = Firebase.firestore
        acct = GoogleSignIn.getLastSignedInAccount(this)
        friendCallName = findViewById(R.id.friend_call_name)
        friendCallLayout = findViewById(R.id.friend_call_layout)
        btn_callAccept = findViewById(R.id.friend_call_accept)
        btn_callDeny = findViewById(R.id.friend_call_deny)


        webViewFlag = true

        bottomNavigationView = findViewById(R.id.main_nav)

        val navController = findNavController(R.id.navController)
        bottomNavigationView.setupWithNavController(navController)

//        mainPageLayout.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                hideKeyboard()
//                return false
//            }
//        })


        //콜 받는곳
        callRef = db.collection("Friends").document(acct.email)
        callRef.addSnapshotListener { snapshots, e ->
                val friendInfo = snapshots?.data?.keys

                if (friendInfo!!.size >= 2) {
                    for (i in friendInfo!!) {
                        if(i == "request"){
                            var friendName: String? = null
                            snapshots?.getString("request")?.let{
                                if(it == "disconnected"){
                                    friendCallDataDelete()
                                }else if(it.isNullOrEmpty()){
                                    friendCallLayout.visibility = View.GONE
                                }else{
                                    friendVideoCallRoomName = it
                                    Log.d("친구콜 요청받음", friendVideoCallRoomName!!)
                                }
                            }
                            snapshots?.getString("requestName")?.let{
                                if(it == "disconnected"){
                                    friendCallDataDelete()
                                }else if(it.isNullOrEmpty()){
                                    friendCallLayout.visibility = View.GONE
                                }else{
                                    friendName = it
                                    Log.d("친구콜 요청받음", friendName!!)
                                }
                            }

                            friendCallName.text = friendName.plus("님의 통화요청중..")
                            friendCallLayout.visibility = View.VISIBLE
                        }
                    }
                }
            }



        btn_callAccept.setOnClickListener {

            val friendVideoCall = Intent(this, FriendVideoCall::class.java)
            startActivity(friendVideoCall)
            friendCallLayout.visibility = View.GONE
        }

        btn_callDeny.setOnClickListener {
            friendCallDataDelete()
        }



    }

    fun friendCallDataDelete(){
        val updates = hashMapOf<String, Any>(
            "request" to FieldValue.delete(),
            "requestName" to FieldValue.delete()
        )

        callRef!!.update(updates)
            .addOnCompleteListener {
                friendCallLayout.visibility = View.GONE
            }
    }

//    fun hideKeyboard() { //가끔 메인화면에서 팅기는 이유는 이 함수 때문...
//        val inputManager: InputMethodManager =
//            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputManager.hideSoftInputFromWindow(
//            this.currentFocus!!.windowToken,
//            InputMethodManager.HIDE_NOT_ALWAYS
//        )
//    }
}