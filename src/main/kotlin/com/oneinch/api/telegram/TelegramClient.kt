package com.oneinch.api.telegram

import com.github.openjson.JSONObject
import com.oneinch.api.one_inch.logErrorMessage

class TelegramClient(val telegramApi: TelegramApi) {

    fun sendMessage(message: String): JSONObject? {
        val response = telegramApi.sendMessage(message).execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            response.logErrorMessage("Error sending message.")
            null
        }
    }
}