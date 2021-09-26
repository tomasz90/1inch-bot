package com.oneinch.api.telegram

import com.github.openjson.JSONObject
import com.oneinch.api.one_inch.logErrorMessage
import org.springframework.stereotype.Component

@Component
class TelegramClient(val telegramApi: TelegramApi) {

    fun sendMessage(amount: Double): JSONObject? {
        val message = "You earned $amount$  \uD83D\uDCB8"
        val response = telegramApi.sendMessage(message).execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            response.logErrorMessage("Error sending message.")
            null
        }
    }
}