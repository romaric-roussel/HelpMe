package com.example.helpme.Data


import com.example.helpme.Data.Model.MessageResult
import retrofit2.Call
import retrofit2.http.*


interface API {

    @POST("message")
    @FormUrlEncoded
    fun newMessage(
            @Field("author") author: String,
            @Field("message") message: String): Call<MessageResult>





}