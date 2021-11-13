package com.example.imfine

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ChatRecyclerViewAdapter(private val context: Context) : RecyclerView.Adapter<ChatRecyclerViewAdapter.ViewHolder>() {
    private var userList = mutableListOf<ChatArea>()

    fun setListData(data:MutableList<ChatArea>){
        userList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_chat,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRecyclerViewAdapter.ViewHolder, position: Int) {
        val user : ChatArea = userList[position]
        holder.nickName.text = user.nickName
        holder.msg.text = user.msg
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val nickName : TextView = itemView.findViewById(R.id.nickName)
        val msg: TextView= itemView.findViewById(R.id.msg)

    }

}