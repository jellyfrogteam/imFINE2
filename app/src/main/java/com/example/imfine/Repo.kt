package com.example.imfine

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Repo {
    //*Repository : datasource를 캡슐화 하는 것

    fun getData(): LiveData<MutableList<ChatArea>> {
        val mutableData = MutableLiveData<MutableList<ChatArea>>()
        val database = Firebase.database
        val myRef = database.getReference("User")
//        myRef.addValueEventListener(object : ValueEventListener {
//            val listData: MutableList<ChatArea> = mutableListOf<ChatArea>()
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if (snapshot.exists()){
//                    for (userSnapshot in snapshot.children){
//                        val getData = userSnapshot.getValue(ChatArea::class.java)
//                        listData.add(getData!!)
//
//                        Log.d("snapShot", listData.toString())
//                        mutableData.value = listData
//                        Log.d("snapShot22222222222", mutableData.value.toString())
//                    }
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })

        myRef.addChildEventListener(object : ChildEventListener{
            val listData: MutableList<ChatArea> = mutableListOf<ChatArea>()

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val getData = snapshot.getValue(ChatArea::class.java)
                listData.add(getData!!)

                Log.d("snapShot", listData.toString())
                mutableData.value = listData
                Log.d("snapShot22222222222", mutableData.value.toString())
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onChildRemoved(snapshot: DataSnapshot) { }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) { }

            override fun onCancelled(error: DatabaseError) { }

        })

        return mutableData
    }


}