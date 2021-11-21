package com.example.imfine

import android.annotation.SuppressLint
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
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*
import org.w3c.dom.Text

class ProfileFragment : Fragment() {
    private var mAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Inflate the layout for this fragment
        val btn_google_exit : Button? = view.findViewById(R.id.btn_google_exit)
        val Version : TextView? = view.findViewById(R.id.Version)
        val QnA : TextView? = view.findViewById(R.id.QnA)
        val source : TextView? = view.findViewById(R.id.source)
        var sw = true
        val login = Intent(activity,login::class.java)

        val nameContent: TextView = view.findViewById(R.id.profile_name_content)
        val emailContent: TextView = view.findViewById(R.id.profile_email_content)
        val acct = GoogleSignIn.getLastSignedInAccount(context)

        nameContent.text = acct.displayName
        emailContent.text = acct.email



        mAuth = FirebaseAuth.getInstance()
        btn_google_exit?.setOnClickListener {
            revokeAccess()
            Toast.makeText(activity,"구글 탈퇴 성공", Toast.LENGTH_LONG).show()
            startActivity(login)
            activity?.finish()

        }
        Version?.setOnClickListener {
            if (sw){
                Version.text = "1.0.0"
                sw = false
            }
            else{
                Version.text = "버전 정보"
                sw = true
            }
        }
        QnA?.setOnClickListener {
            if (sw){
                QnA.text = "jellyfrogteam@gmail.com"
                sw = false
            }
            else{
                QnA.text = "문의하기"
                sw = true
            }
            source?.setOnClickListener {
                if (sw){
                    source.text = "flaticon | lottie | hatchful"
                    sw = false
                }
                else{
                    source.text = "출처확인"
                    sw = true
                }
            }
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