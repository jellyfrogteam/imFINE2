package com.example.imfine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.mypage.*

class mypage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mypage)

        val login = Intent(this,login::class.java)
        google_logout.setOnClickListener {
                signOut()
                Toast.makeText(this,"구글 로그아웃 성공", Toast.LENGTH_LONG).show()
                startActivity(login)
                finish()
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
}