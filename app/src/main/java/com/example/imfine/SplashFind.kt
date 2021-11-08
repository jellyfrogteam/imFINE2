package com.example.imfine

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.splashfind.*

class SplashFind : AppCompatActivity() {

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
        animationView_find.setOnClickListener {
            accountAvailable(videoCall)
        }

        db.collection("rooms")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                    Room_ID = document.id
                    roomExists = true
                    Log.d("Adad", Room_ID!!)
                    break
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

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
        }, 1000)
    }
}
