package com.example.imfine

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object{
        var Room_ID : String? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val videoCall = Intent(this, VideoCall::class.java)
        val mypage = Intent(this, mypage::class.java)
        val db = Firebase.firestore



        //모든 화면회전금지
        //상태바, 네비게이션바 제거
        //야간모드 해제

        //액티비티와 프래그먼트 통신할때 -> startActivityForResult사용
//      액티비티 a를 거치는 방법
//        프래그먼트에서 메소드로 데이터를 액티비티a로 전달 후 액티비티a에서 인텐트를 액티비티b로 startActivityForResult를 이용해 넘겨준다면,
//        액티비티b에서 setResult를 하면 프래그먼트가 아닌 액티비티a로 돌아온다.
//        그럼 전달받은 데이터를 다시 액티비티a에서 메소드로 프래그먼트에 넘겨주면 프래그먼트와 액티비티b의 통신이 이루어진다.



        db.collection("rooms")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    Room_ID = document.id
                    Log.d("Adad", Room_ID!!)
                    break
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

        test_btn.setOnClickListener {
            startActivity(videoCall)
            finish()
        }

        myPage_btn.setOnClickListener {
            startActivity(mypage)
            finish()
        }
    }
}