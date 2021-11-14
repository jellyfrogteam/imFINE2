package com.example.imfine

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.videocall.*


class SplashFind : AppCompatActivity() {

    private val FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0
    private var randomNum = (Math.random() * 6).toLong()
    private var randomNum2 = (Math.random() * 6).toLong()

    companion object{
        var Room_ID : String? = null
        var roomExists: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashfind)

        //val login = Intent(this, login::class.java)
        val videoCall = Intent(this, VideoCall::class.java)

        val db = Firebase.firestore

        Log.d("randomNum", randomNum.toString())
        val handler = Handler()
        handler.postDelayed(Runnable {
            db.collection("rooms")
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        for (document in result) {
                            Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                            Room_ID = document.id
                            roomExists = true
                            Log.d("Adad", Room_ID!!)
                            Log.d("roomTest", "파이어베이스에서 방 찾았음${Room_ID.toString()}")
                            break
                        }
                        accountAvailable(videoCall)
                    }else {
                        Room_ID = ""
                        roomExists = false
                        Log.d("roomTest", "파이어베이스에서 방 못찾음${Room_ID.toString()}")
                        accountAvailable(videoCall)
                    }

                }
                .addOnFailureListener { exception ->
                    Log.w(ContentValues.TAG, "Error getting documents.", exception)
                }
        }, 0) //딜레이 타임 조절

    }

    private fun accountAvailable(intent: Intent) {
        /*  Log.d("dddddd","$splashFlag")
          var duration: Long = if(splashFlag) 100 else 3000
          Log.d("dddddd","$duration")*/

        Handler().postDelayed({
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            overridePendingTransition(anim.fade_in, anim.fade_out)
            startActivity(intent)
            finish()
        }, 4000)
    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        finish()
//    }

    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime: Long = tempTime - backPressedTime
        if (intervalTime in 0..FINISH_INTERVAL_TIME) {
            super.onBackPressed()
            //moveTaskToBack(true)		// 태스크를 백그라운드로 이동
            finishAndRemoveTask()				// 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid())
        } else {
            backPressedTime = tempTime
            Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 매칭이 종료됩니다", Toast.LENGTH_SHORT).show()
        }
    }

}
