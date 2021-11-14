package com.example.imfine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imfine.databinding.FragmentFriendBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn

class FriendFragment : Fragment() {
    private var _binding: FragmentFriendBinding? = null
    private val binding get() = _binding!!
    private val chatList = arrayListOf<friendLayout>()    // 리사이클러 뷰 목록
    private lateinit var adapter: friendAdapter
    private val currentUser = "dad"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.friendRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = friendAdapter(currentUser, chatList)
        binding.friendRv.adapter = adapter

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val img = R.drawable.ic_person
        val personName : String
        val acct = GoogleSignIn.getLastSignedInAccount(context)
        personName = acct.displayName
        val item = friendLayout(img, personName)
        chatList.add(item)
        chatList.add(item)
        chatList.add(item)
        chatList.add(item)
        chatList.add(item)
        chatList.add(item)

        adapter.notifyDataSetChanged()

    }

}