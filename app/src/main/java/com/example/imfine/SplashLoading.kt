package com.example.imfine

import android.Manifest
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.splashloading.*


class SplashLoading : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashloading)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val login = Intent(this, login::class.java)
        val mainPage = Intent(this, MainPage::class.java)


        checkPermission()

        animationView.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser?.uid != null) {
                accountAvailable(mainPage)
            } else {
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

    private fun checkPermission() {

        val permissions = arrayOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE
        )

        for (i in permissions) {
            val per = this.let {
                ContextCompat.checkSelfPermission(
                    it,
                    i
                )
            }
            if (per != PackageManager.PERMISSION_GRANTED) {
                // ????????? ?????? ?????? permission ????????? ????????? ???????????? ?????????.
                this.let {
                    ActivityCompat.requestPermissions(
                        it,
                        permissions,
                        1000
                    )
                }
            } else {
                // ????????? ?????? ??????
                // Toast.makeText(this, "?????????,audio??? ???????????????", Toast.LENGTH_LONG).show()
            }
        }

    }

    //????????? ?????? ???????????? ???????????? Deny Eh?????? ????????? ???????????? ??????????????? onRequestPermissionResult()??? ????????????. ??? ????????? ????????? ?????? ??? ???????????? ??????.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Toast ??? ?????????
                Toast.makeText(this, "?????????,audio??? ???????????????", Toast.LENGTH_LONG).show()
            } else {
                // ???????????? ?????????
                finish()
            }
        }
    }
}