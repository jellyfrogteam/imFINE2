package com.example.imfine

import android.content.Context
import android.content.Intent
import android.graphics.Movie
import android.opengl.Visibility
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.videocall.*


class friendAdapter(val currentUser: String,
                    private val itemList: ArrayList<friendLayout>,
                    private val items: ArrayList<friendLayout>): RecyclerView.Adapter<friendAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): friendAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.friend_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: friendAdapter.ViewHolder, position: Int) {

        holder.username.text = itemList[position].username

        val item: friendLayout = items[position]
        Glide.with(holder.itemView.context)
            .load(item.img).apply(RequestOptions().circleCrop())
            .into(holder.img)

        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.chat_card_view)
        var img: ImageView = itemView.findViewById(R.id.img)
        val username: TextView = itemView.findViewById(R.id.username)
        val btn_videoCall: ImageButton = itemView.findViewById(R.id.btn_videocall)
        init {

            itemView.setOnClickListener {
                //리사이클러뷰 아이템 클릭시
            }

            val handler = Handler()
            handler.postDelayed(Runnable {
                var myFriendEmailList = FriendFragment.myFriendEmailList
                Log.d("친구콜 내친구목록", myFriendEmailList.toString())

                val db = Firebase.firestore
                val acct = GoogleSignIn.getLastSignedInAccount(itemView.context)
                val localFriendRef = db.collection("Friends").document(acct.email)

                val callInfo = mapOf(
                    "request" to "${acct.id}",
                    "requestName" to "${acct.displayName}"
                    //이메일이 파이어스토어에 등록될때 aaa@gmail.com이 aaa@gmail하고 .com으로 나눠짐;;
                )

                btn_videoCall.setOnClickListener {
                    val friendEmail = myFriendEmailList[adapterPosition]
                    Log.d("친구콜 선택된 친구목록", myFriendEmailList[adapterPosition])


                    db.collection("Friends").document(friendEmail)
                        .update(callInfo)
                        .addOnSuccessListener {
                            //웹뷰 이동
                            MainPage.webViewFlag = false
                            val friendVideoCall = Intent(itemView.context, FriendVideoCall::class.java)
                            startActivity(itemView.context, friendVideoCall, null)
                        }.addOnFailureListener {

                        }
                }



            }, 500) //딜레이 타임 조절

        }
    }

}