package com.oneinch.one_inch_api

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.config.Settings
import com.oneinch.one_inch_api.api.OneInchApi
import com.oneinch.one_inch_api.api.data.QuoteDto
import com.oneinch.one_inch_api.api.data.SwapDto
import com.oneinch.one_inch_api.api.data.toDto
import com.oneinch.util.getLogger
import org.springframework.stereotype.Component
import retrofit2.Response

@Component
class OneInchClient(val myAddress: String, val oneInch: OneInchApi, val settings: Settings) {

    fun quote(chainId: Int, from: TokenQuote, to: Token): QuoteDto? {
        val response = oneInch.quote(chainId, from.address, to.address, from.origin).execute()
        if (response.isSuccessful) {
            return response.body()!!.toDto()
        } else {
            response.logErrorMessage("Error during quote.")
            return null
        }
    }

    fun swap(chainId: Int, from: TokenQuote, to: Token, maxSlippage: Double, allowPartialFill: Boolean): SwapDto? {
        val response =
            oneInch.swap(
                chainId,
                from.address,
                to.address,
                from.origin,
                myAddress,
                maxSlippage,
                allowPartialFill
            ).execute()
        if (response.isSuccessful) {
            return response.body()!!.toDto()
        } else {
            response.logErrorMessage("Error during swap.")
            return null
        }
    }
}

fun <T> Response<T>.logErrorMessage(info: String) {
    val text = this.errorBody()!!.charStream().readText()
    getLogger().error("$info Response status: ${this.code()} $text")
}


