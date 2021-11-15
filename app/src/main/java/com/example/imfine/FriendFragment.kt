package com.example.imfine

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imfine.databinding.FragmentFriendBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.friend_layout.*


class FriendFragment : Fragment() {
    private var _binding: FragmentFriendBinding? = null
    private val binding get() = _binding!!
    private val friendList = arrayListOf<friendLayout>()    // 리사이클러 뷰 목록
    private lateinit var adapter: friendAdapter
    private val currentUser = "dad"
    private var items = arrayListOf<friendLayout>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFriendBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.friendRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = friendAdapter(currentUser, friendList, items)
        binding.friendRv.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        val view2 = LayoutInflater.from(context).inflate(R.layout.friend_layout, null, false)
        val imageView =view2.findViewById<ImageView>(R.id.img)
        val acct = GoogleSignIn.getLastSignedInAccount(context)
        val personImgUrl = acct.photoUrl
        val personName = acct.displayName


        val item = friendLayout(personImgUrl.toString(), personName)
        friendList.add(item) //구글 이름
        items.add(item) // 사진url
        adapter.notifyDataSetChanged()


        binding.addFriend.setOnClickListener {
            val friendEmail = binding.friendEmail.text
            val friendInfo = mapOf(
                "${acct.email}" to "ada"
            )

            db.collection("Friends").document(friendEmail.toString())
                .update(friendInfo)
                .addOnSuccessListener {

                }.addOnFailureListener {

                }
        }

        binding.btnTest.setOnClickListener {
            val docRef = db.collection("Friends").document("ddd@naver.com")
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d(TAG, "No such document")
                    }
                    Log.d(TAG, document.data?.keys.toString())
                    Log.d(TAG, document.data?.values.toString())
                   // Log.d(TAG, document.data?.)
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }
    }

}