package com.oneinch.oneinch_api.api

import com.oneinch.oneinch_api.api.data.QuoteResponse
import com.oneinch.oneinch_api.api.data.SwapResponse
import getLogger
import org.json.JSONObject
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import retrofit2.Response

class QuoteException(val response: Response<QuoteResponse>) : Throwable()

class SwapException(val response: Response<SwapResponse>) : Throwable()

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(QuoteException::class)
    fun handleQuote(response: Response<QuoteResponse>) {
        response.logErrorMessage("Error during quote.")
    }

    @ExceptionHandler(SwapException::class)
    fun handleSwap(response: Response<SwapResponse>) {
        response.logErrorMessage("Error during swap.")
    }
}

fun <T> Response<T>.logErrorMessage(info: String) {
    val msg = JSONObject(this.errorBody()!!.charStream().readText()).getString("message")
    getLogger().error("$info Response status: ${this.code()}\n $msg")
}