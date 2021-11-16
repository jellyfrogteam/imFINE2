package com.example.imfine

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.google.firestore.v1.DocumentChange
import kotlinx.android.synthetic.main.main.*


class MainPage : AppCompatActivity(){

    private lateinit var bottomNavigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        bottomNavigationView = findViewById(R.id.main_nav)

        val navController = findNavController(R.id.navController)
        bottomNavigationView.setupWithNavController(navController)

//        mainPageLayout.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                hideKeyboard()
//                return false
//            }
//        })


        //친구요청 대기



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