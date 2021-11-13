package com.example.imfine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatListViewModel: ViewModel() {
    //viewModel은 repo에 있는 데이터를 관찰하고 있다가 변경이 되면 mutableData의 값을 변경시켜주는 역할

    private val repo = Repo()
    fun fetchData(): LiveData<MutableList<ChatArea>> {
        val mutableData = MutableLiveData<MutableList<ChatArea>>()
        repo.getData().observeForever{
            mutableData.value = it
        }
        return mutableData
    }
}