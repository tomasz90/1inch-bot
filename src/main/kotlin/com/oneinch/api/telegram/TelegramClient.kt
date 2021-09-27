package com.oneinch.api.telegram

import com.github.openjson.JSONObject
import com.oneinch.api.one_inch.logErrorMessage
import com.oneinch.loader.Properties
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("realAccount")
class TelegramClient(val telegramApi: TelegramApi, val properties: Properties) {

    fun sendSwapMessage(amount: Double, gas: Double?) {
        var message = "Congratulations, you earned $amount$  \uD83D\uDCB8"
        if (gas != null) {
            message += "\nRemaining gas: $gas matic."
        }
        sendMessage(message)
    }

    fun sendRefillGasBalanceMessage(gas: Double) {
        val message = "Refilled gas balance. \nNew balance: $gas matic  \uD83D\uDCA7"
        sendMessage(message)
    }

    fun sendNoTransactionsMessage(hours: Long) {
        var message = "No transaction in last $hours hours...  \uD83D\uDE1F"
        message += "Changing advantage to 0.0."
        sendMessage(message)
    }

    private fun sendMessage(message: String): JSONObject? {
        val response = telegramApi.sendMessage(properties.telegramToken, message).execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            response.logErrorMessage("Error sending message.")
            null
        }
    }
}