package com.example.imfine

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.imfine.FriendFragment.Companion.friendList
import com.example.imfine.FriendFragment.Companion.items
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AddFriendAdapter(val currentUser: String,
                       private val itemList: ArrayList<AddFriendLayout>,
                       private val items: ArrayList<AddFriendLayout>): RecyclerView.Adapter<AddFriendAdapter.ViewHolder>() {

//    //클릭리스너
//    private lateinit var itemClickListener: ItemClickListener
//
//    interface ItemClickListener{
//        fun onClick(view : View, position: Int)
//    }
//
//    fun setItemClickListener(itemClickListener: ItemClickListener) {
//        this.itemClickListener = itemClickListener
//    }
//
//    interface OnViewHolderItemClickListener {
//        fun onViewHolderItemClick()
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddFriendAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.addfriend_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddFriendAdapter.ViewHolder, position: Int) {

        holder.username.text = itemList[position].username_Add

        val item: AddFriendLayout = items[position]
        val imgUrl = item.img_Add
        Log.d("FirebaseURL", imgUrl)
        Glide.with(holder.itemView.context)
            .load(imgUrl).apply(RequestOptions().circleCrop())
            .error(R.drawable.logo)
            .into(holder.img)

//        holder.itemView.setOnClickListener {
//            notifyItemChanged(position)
//            itemClickListener.onClick(it,position)
//        }
    }

    override fun getItemCount(): Int {

        return itemList.size
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.chat_card_view)
        var img: ImageView = itemView.findViewById(R.id.img_add)
        val username: TextView = itemView.findViewById(R.id.username_add)
        val btnPermit: ImageButton = itemView.findViewById(R.id.btn_permit)
        val btnDeny: ImageButton = itemView.findViewById(R.id.btn_deny)


        init{

            btnPermit.setOnClickListener {
//                FriendFragment.friendPosition(position)
//                FriendFragment.friendType(true)

                val friendEmailList = FriendFragment.friendEmailList
                friendEmailList.remove("tmp")
                //홀수인덱스: 이메일, 짝수인덱스: 이름
                Log.d("friendEmailFull", friendEmailList.toString())
                Log.d("friendEmailFull", adapterPosition.toString())
                Log.d("friendEmailFullINDEX", friendEmailList[adapterPosition*2])
                val emailList = friendEmailList[adapterPosition*2]


                val db = Firebase.firestore
                val acct = GoogleSignIn.getLastSignedInAccount(itemView.context)


                val localFriendRef = db.collection("Friends").document(acct.email)
                localFriendRef.get()
                    .addOnSuccessListener {
//                var name = ""
//                var photoUrl = ""
//                var status = ""
                        for (i in it.data?.keys!!) {
                            val emailFull = i.toString().plus(".com")

                            if (emailFull == emailList) {
                                    it?.getString(emailFull)?.let {
                                        Log.d("FirestoreTest", it)
                                        val values: List<String> = it.split(",")
                                        val name = values[0]
                                        val photoUrl = values[1].trim()
                                        val status = values[2].trim()
                                        Log.d(
                                            "FirestoreTestVALUES",
                                            "name: ${name}, photoUrl: ${photoUrl}, status: ${status}"
                                        )

                                        localFriendRef.update(
                                            emailFull,
                                            name.plus(", ").plus(photoUrl).plus(", ").plus("수락됨")
                                        )
                                            .addOnSuccessListener {
                                                Log.d("UpdateTest", name.plus(", ").plus(photoUrl).plus(", ").plus("수락됨"))
                                                friendEmailList.remove(emailList)
                                                friendEmailList.remove(name)
                                                Log.d("UpdateTest", friendEmailList.toString())
                                            }
                                            .addOnFailureListener {

                                            }

                                    }
                            }
                        }
                    }
            }

            btnDeny.setOnClickListener {
//                FriendFragment.friendPosition(position)
//                FriendFragment.friendType(false)
            }

        }

    }

}