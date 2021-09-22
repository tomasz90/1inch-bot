package com.oneinch.api.one_inch

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.api.one_inch.api.OneInchApi
import com.oneinch.api.one_inch.api.data.QuoteDto
import com.oneinch.api.one_inch.api.data.SwapDto
import com.oneinch.api.one_inch.api.data.toDto
import com.oneinch.config.Settings
import com.oneinch.util.getLogger
import org.springframework.stereotype.Component
import retrofit2.Response

@Component
class OneInchClient(val myAddress: String, val oneInch: OneInchApi, val settings: Settings, val chain: Chain) {

    fun quote(from: TokenQuote, to: Token): QuoteDto? {
        val response = oneInch.quote(chain.id, from.token.address, to.address, from.origin).execute()
        return if (response.isSuccessful) {
            response.body()!!.toDto()
        } else {
            response.logErrorMessage("Error during quote.")
            null
        }
    }

    fun swap(from: TokenQuote, to: Token, allowPartialFill: Boolean, protocols: String): SwapDto? {
        val response =
            oneInch.swap(
                chain.id,
                from.token.address,
                to.address,
                from.origin,
                myAddress,
                settings.defaultSlippage,
                allowPartialFill,
                protocols
            ).execute()
        return if (response.isSuccessful) {
            response.body()!!.toDto()
        } else {
            response.logErrorMessage("Error during swap.")
            null
        }
    }
}

fun <T> Response<T>.logErrorMessage(info: String) {
    val text = this.errorBody()!!.charStream().readText()
    getLogger().error("$info Response status: ${this.code()} $text")
}


