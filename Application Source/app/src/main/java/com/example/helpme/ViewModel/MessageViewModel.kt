package com.example.helpme.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helpme.Repository.MessageRepository
import com.example.helpme.Data.Model.MessageResult


class MessageViewModel : ViewModel() {

    //var userResultData = MutableLiveData<UserResultData>()




    fun newMessage(author:String,message:String) : MutableLiveData<MessageResult> {
        return MessageRepository.fetchMessage(author, message)
    }

}