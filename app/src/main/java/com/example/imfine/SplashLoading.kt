package com.example.imfine

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.splashloading.*
import kotlinx.android.synthetic.main.splashloading.*

class SplashLoading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashloading)
        val login = Intent(this, login::class.java)
        val mainPage = Intent(this,MainPage::class.java)

            animationView.setOnClickListener {
                if (FirebaseAuth.getInstance().currentUser?.uid != null) {
                    accountAvailable(mainPage)
                }
                else
                {
                    accountAvailable(login)
                }
            }


    }

    private fun accountAvailable(intent: Intent) {
      /*  Log.d("dddddd","$splashFlag")
        var duration: Long = if(splashFlag) 100 else 3000
        Log.d("dddddd","$duration")*/

        Handler().postDelayed({
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            overridePendingTransition(anim.fade_in, anim.fade_out)
            finish()
        }, 1000)
    }
}