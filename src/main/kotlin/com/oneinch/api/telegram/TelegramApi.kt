package com.oneinch.api.telegram

import com.github.openjson.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val token = "***REMOVED***"

interface TelegramApi {

    @GET("https://api.telegram.org/bot$token/sendMessage")
    fun sendMessage(
        @Query("text") text: String,
        @Query("chatId") chatId: Long = -577856646L
    ): Call<JSONObject>
}