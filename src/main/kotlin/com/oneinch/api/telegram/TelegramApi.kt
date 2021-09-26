package com.oneinch.api.telegram

import com.github.openjson.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val token = "***REMOVED***"
const val baseUrl = "https://api.telegram.org"

interface TelegramApi {

    @GET("/bot$token/sendMessage")
    fun sendMessage(
        @Query("text") text: String,
        @Query("chat_id") chatId: Long = -577856646L
    ): Call<JSONObject>
}