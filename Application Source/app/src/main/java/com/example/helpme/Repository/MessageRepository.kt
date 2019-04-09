package com.example.helpme.Repository

import androidx.lifecycle.MutableLiveData
import com.example.helpme.Data.GestionRetrofit
import com.example.helpme.Data.Model.MessageResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MessageRepository {

    val messageData = MutableLiveData<MessageResult>()
    private val api = GestionRetrofit.initRetrofit()

    fun fetchMessage(author:String,message:String): MutableLiveData<MessageResult>{

        val call= api.newMessage(author,message)

        call.enqueue(object : Callback<MessageResult>{
            override fun onFailure(call: Call<MessageResult>, t: Throwable) {
                //TODO
                messageData.postValue(null)
            }

            override fun onResponse(call: Call<MessageResult>, response: Response<MessageResult>) {
                messageData.postValue(response.body())
            }

        })
        return messageData
    }



}