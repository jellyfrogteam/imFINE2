package com.example.imfine

import android.content.Context
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
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.chat_card_view)
        var img: ImageView = itemView.findViewById(R.id.img)
        val username: TextView = itemView.findViewById(R.id.username)
        val btn_videoCall: ImageButton = itemView.findViewById(R.id.btn_videocall)
        val btn_friend_delete: ImageButton = itemView.findViewById(R.id.btn_friend_delete)
        val btn_friend_check: ImageButton = itemView.findViewById(R.id.btn_check)

        init {

            val handler = Handler()
            handler.postDelayed(Runnable {
                val db = Firebase.firestore
                val acct = GoogleSignIn.getLastSignedInAccount(itemView.context)
                val localFriendRef = db.collection("Friends").document(acct.email)

                val friendEmailListLocal = FriendFragment.friendEmailListLocal
                friendEmailListLocal.remove("tmp")
                //홀수인덱스: 이메일, 짝수인덱스: 이름
                Log.d("친구창", friendEmailListLocal.toString())
                Log.d("친구창(포지션)", adapterPosition.toString())
                Log.d("친구창", friendEmailListLocal[adapterPosition])
                val emailList = friendEmailListLocal[adapterPosition]


                btn_friend_delete.setOnClickListener {
                    Log.d("친구창(포지션)", adapterPosition.toString())
                    Log.d("친구창(클릭한항목의 이메일)", emailList)
                    //본인 계정에서 지우고싶은 이메일(친구) 삭제
                    Toast.makeText(itemView.context, "정말로 삭제 하시겠습니까??", Toast.LENGTH_SHORT).show()
                    btn_friend_check.visibility = View.VISIBLE
                }
                btn_friend_check.setOnClickListener {
                    Log.d("친구창(포지션)", adapterPosition.toString())
                    val updates = hashMapOf<String, Any>(
                        emailList to FieldValue.delete()
                    )

                    localFriendRef.update(updates)
                        .addOnCompleteListener {
                            friendEmailListLocal.remove(emailList)
                            btn_friend_check.visibility = View.GONE
                            btn_friend_delete.visibility = View.GONE
                            btn_videoCall.visibility = View.GONE
                            Toast.makeText(itemView.context, "새로고침 해주세요", Toast.LENGTH_SHORT)
                        }

                    //FriendFragment.adapter.notifyDataSetChanged()
                }

                FriendFragment.btnRefresh.setOnClickListener {
                    btn_friend_check.visibility = View.GONE
                    btn_friend_delete.visibility = View.VISIBLE
                    btn_videoCall.visibility = View.VISIBLE
                }

            }, 1500) //딜레이 타임 조절
        }
    }

}