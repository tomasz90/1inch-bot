package com.oneinch.api.telegram

import com.github.openjson.JSONObject
import com.oneinch.api.one_inch.logErrorMessage
import com.oneinch.loader.Properties
import org.springframework.stereotype.Component

@Component
class TelegramClient(val telegramApi: TelegramApi, val properties: Properties) {

    fun sendMessage(amount: Double, gas: Double): JSONObject? {
        val message = "Congratulations, you earned $amount$  \uD83D\uDCB8" +
                "\nRemaining gas: $gas matic."
        val response = telegramApi.sendMessage(properties.telegramToken, message).execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            response.logErrorMessage("Error sending message.")
            null
        }
    }
}