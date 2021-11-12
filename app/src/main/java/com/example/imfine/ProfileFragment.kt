package com.example.imfine

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val google_logout : Button? = activity?.findViewById(R.id.google_logout)
        val login = Intent(activity,login::class.java)
        google_logout?.setOnClickListener {
            signOut()
            Toast.makeText(activity,"구글 로그아웃 성공", Toast.LENGTH_LONG).show()
            startActivity(login)
            activity?.finish()

        }
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }

}