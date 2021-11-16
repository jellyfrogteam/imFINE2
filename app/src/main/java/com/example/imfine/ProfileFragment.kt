package com.example.imfine

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {
    private var mAuth: FirebaseAuth? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Inflate the layout for this fragment
        val btn_google_exit : Button? = view.findViewById(R.id.btn_google_exit)
        val login = Intent(activity,login::class.java)
        mAuth = FirebaseAuth.getInstance()
        btn_google_exit?.setOnClickListener {
            revokeAccess()
            Toast.makeText(activity,"구글 탈퇴 성공", Toast.LENGTH_LONG).show()
            startActivity(login)
            activity?.finish()

        }
//        view.setOnTouchListener(object : View.OnTouchListener {
//            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
//                hideKeyboard()
//                return false
//            }
//        })

        return view
    }
  /*  private fun signOut() {
        FirebaseAuth.getInstance().signOut()
    }
*/
    private fun revokeAccess() {
        mAuth?.currentUser?.delete()
    }
//    fun hideKeyboard() { //가끔 메인화면에서 팅기는 이유는 이 함수 때문...
//        val inputManager: InputMethodManager =
//            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        inputManager.hideSoftInputFromWindow(
//            activity?.currentFocus!!.windowToken,
//            InputMethodManager.HIDE_NOT_ALWAYS
//        )
//    }

}