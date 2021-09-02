package com.oneinch.oneinch_api

import com.oneinch.config.InputConfig.MAX_SLIPPAGE
import com.oneinch.oneinch_api.api.ApiConfig.ONE_INCH_API
import com.oneinch.oneinch_api.api.data.*
import org.json.JSONObject
import org.springframework.stereotype.Component
import retrofit2.Response

@Component
class OneInchClient(private val myAddress: String) {

    fun quote(chainId: Int, from: TokenQuote, to: Token): QuoteDto {
        val response = ONE_INCH_API.quote(chainId, from.token.address, to.address, from.origin).execute()
        if (response.isSuccessful) {
            return response.body()!!.toDto()
        } else {
            val msg = response.getErrorMessage("Error during quote.")
            throw QuoteException(msg)
        }
    }

    fun swap(chainId: Int, from: TokenQuote, to: Token): SwapDto {
        val response =
            ONE_INCH_API.swap(chainId, from.token.address, to.address, from.origin, myAddress, MAX_SLIPPAGE).execute()
        if (response.isSuccessful) {
            return response.body()!!.toDto()
        } else {
            val msg = response.getErrorMessage("Error during swap.")
            throw SwapException(msg)
        }
    }
}

class QuoteException(s: String) : Throwable(s)

class SwapException(s: String) : Throwable(s)

fun <T> Response<T>.getErrorMessage(info: String): String {
    val msg = JSONObject(this.errorBody()!!.charStream().readText()).getString("message")
    return "$info Response status: ${this.code()}\n $msg"
}


