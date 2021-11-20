package com.example.imfine

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imfine.databinding.FragmentFriendBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_friend.*
import kotlinx.android.synthetic.main.friend_layout.*
import java.text.FieldPosition


class FriendFragment : Fragment() {
    private lateinit var db: FirebaseFirestore
    private lateinit var acct: GoogleSignInAccount

    private var _binding: FragmentFriendBinding? = null
    private val binding get() = _binding!!
    private val currentUser = "dad"

    private lateinit var adapter2: AddFriendAdapter
    private val addFriendList = arrayListOf<AddFriendLayout>()
    private var addItems = arrayListOf<AddFriendLayout>()

    companion object{
//        fun friendPosition(position: Int): Int {
//            Log.d("Position", "pos: ${position}")
//            return position
//        }
//        fun friendType(type: Boolean): Boolean {
//            Log.d("Position", "type: ${type}")
//            return type
//        }
        lateinit var adapter: friendAdapter

        val friendList = arrayListOf<friendLayout>()    // 리사이클러 뷰 목록
        var items = arrayListOf<friendLayout>()

        var friendEmailList: ArrayList<String> = arrayListOf("tmp")
        var friendEmailListLocal: ArrayList<String> = arrayListOf("tmp")

        lateinit var btnRefresh: ImageButton

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        db = Firebase.firestore
        acct = GoogleSignIn.getLastSignedInAccount(context)


        _binding = FragmentFriendBinding.inflate(inflater, container, false)
        val view = binding.root

        //친구창
        binding.friendRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = friendAdapter(currentUser, friendList, items)
        binding.friendRv.adapter = adapter

        //친구요청창
        binding.addfriendRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter2 = AddFriendAdapter(currentUser, addFriendList, addItems)
        binding.addfriendRv.adapter = adapter2

        var viewSwitch = true
        binding.btnAddfriendnoti.setOnClickListener {
            if(viewSwitch){
                binding.addFriendLayout.visibility = View.VISIBLE
                viewSwitch = false
            }else{
                binding.addFriendLayout.visibility = View.GONE
                viewSwitch = true
            }
        }


        btnRefresh = binding.btnRefresh


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val database = Firebase.database
        //val myRef = database.getReference("Friends(account)")

        val view2 = LayoutInflater.from(context).inflate(R.layout.friend_layout, null, false)
        val imageView =view2.findViewById<ImageView>(R.id.img)
        val personImgUrl = acct.photoUrl
        val personName = acct.displayName


        //클릭 리스너
        // 서브RV 누르면 삭제
//        adapterRVChecked.setItemClickListener(object : AdditemRVAdapterChecked.ItemClickListener {
//            override fun onClick(view: View, position: Int) {
//
//                val iconRes = indexForDelete[position]  // 이미지 소스 값
//                Log.d("testICON", "pos: ${position}, $iconRes")
//                delImageScrollView(iconRes)
//            }
//        })

        //친구요청 제어




        //친구 신청 //중복은 어차피 안되는듯                  ----------FireStore-------------
        binding.addFriend.setOnClickListener {
            val friendEmail = binding.friendEmail.text
            val friendInfo = mapOf(
                acct.email to "${acct.displayName}, ${acct.photoUrl}, 대기중"
                //이메일이 파이어스토어에 등록될때 aaa@gmail.com이 aaa@gmail하고 .com으로 나눠짐;;
            )
            if (!friendEmail.isNullOrEmpty()) {
                if(friendEmail.toString()!=acct.email){
                    db.collection("Friends").document(friendEmail.toString())
                        .update(friendInfo)
                        .addOnSuccessListener {

                        }.addOnFailureListener {

                        }
                }else{
                    Toast.makeText(context, "본인 이메일로 요청할 수 없습니다", Toast.LENGTH_SHORT)
                }
            }
            binding.friendEmail.setText("")
            Toast.makeText(context, "친구요청이 전송되었습니다", Toast.LENGTH_SHORT)
        }



        btnRefresh.setOnClickListener {
            getDataAndRefresh()
        }


        //데이터 (한번)가져오고 새로고침
        getDataAndRefresh()


        //친구요청 대기                                     ---------FireStore----------

        db.collection("Friends").document(acct.email)
            .addSnapshotListener { snapshots, e ->
                addItems.clear()
                addFriendList.clear()
                friendEmailList.clear()

                val friendInfo = snapshots?.data?.keys
                Log.d("친구요청 대기", snapshots?.data?.keys.toString())
                //Log.d("FirestoreTest", value?.data?.values.toString())

                if (friendInfo!!.size >= 2) {
                    for (i in friendInfo!!) {
                        val emailFull = i.toString().plus(".com")
                        if (emailFull != acct.email) {
                            Log.d("친구요청 대기", i)
                            snapshots?.getString(emailFull)?.let {
                                Log.d("친구요청 대기", it)
                                val values: List<String> = it.split(",")
                                val name = values[0]
                                val photoUrl = values[1].trim()
                                val status = values[2].trim()
                                Log.d(
                                    "친구요청 대기",
                                    "name: ${name}, photoUrl: ${photoUrl}, status: $status"
                                )
                                val itemAdd = AddFriendLayout(photoUrl, name)
                                addItems.add(itemAdd) // 사진url
                                addFriendList.add(itemAdd) //친구이름

                                //arrayList 중복검사
                                if(!friendEmailList.contains(emailFull) && !friendEmailList.contains(name) && !status.isNullOrEmpty()){
                                    friendEmailList.add(emailFull)
                                    friendEmailList.add(name)
                                }

                                Log.d("친구요청 대기(변화값)", friendEmailList.toString())
                                if(status == "수락됨"){
                                    addItems.remove(itemAdd) // 사진url
                                    addFriendList.remove(itemAdd) //친구이름
                                    if(friendEmailList.contains(emailFull) && friendEmailList.contains(name)){
                                        friendEmailList.remove(emailFull)
                                        friendEmailList.remove(name)
                                    }
                                }
                            }
                            adapter2.notifyDataSetChanged()
                        }
                    }
                }



            }
    }

    fun getDataAndRefresh(){
        val localFriend = db.collection("Friends").document(acct.email)
        localFriend.get()
            .addOnSuccessListener {
//                var name = ""
//                var photoUrl = ""
//                var status = ""
                items.clear()
                friendList.clear()
                friendEmailListLocal.clear()

                if (!it.data?.keys.isNullOrEmpty()) {
                    val friendEmail = it.data?.keys
                    for (i in friendEmail!!) {
                        val emailFull = i.toString().plus(".com")
                        it.getString(emailFull).let {
                            val splitData = it?.split(",")
                            val name = splitData?.get(0).toString()
                            val photoUrl = splitData?.get(1).toString().trim()
                            val status = splitData?.get(2).toString().trim()
                            Log.d("정보가져오기(전부)", "name: ${name}, photo: ${photoUrl}, status: ${status}")

                            val item = friendLayout(photoUrl, name)
                            if(status == "수락됨"){
                                Log.d("정보가져오김ㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁㅁ", status)
                                friendList.add(item) //구글 이름
                                items.add(item) // 사진url
                                adapter.notifyDataSetChanged()

                                //arrayList 중복검사
                                if(!friendEmailListLocal.contains(emailFull)){
                                    friendEmailListLocal.add(emailFull)
                                }
                            }else {
                                friendList.remove(item) //구글 이름
                                items.remove(item) // 사진url
                                adapter.notifyDataSetChanged()
                            }
                        }
                    }
                }

            }
            .addOnFailureListener{

            }

        Log.d("정보가져오기(임시배열값)요청창", friendEmailList.toString())
        Log.d("정보가져오기(임시배열값)친구창", friendEmailListLocal.toString())
    }




}