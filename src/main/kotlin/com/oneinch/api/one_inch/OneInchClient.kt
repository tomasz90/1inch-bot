package com.oneinch.api.one_inch

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.api.one_inch.api.OneInchApi
import com.oneinch.api.one_inch.api.data.QuoteDto
import com.oneinch.api.one_inch.api.data.SwapDto
import com.oneinch.api.one_inch.api.data.toDto
import com.oneinch.loader.Settings
import com.oneinch.util.getLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean

@Component
class OneInchClient(
    val myAddress: String,
    val oneInch: OneInchApi,
    val settings: Settings,
    val chain: Chain,
    val scope: CoroutineScope
) {
    private val loweredLimit = AtomicBoolean()

    fun quote(from: TokenQuote, to: Token): QuoteDto? {
        val response = oneInch.quote(chain.id, from.token.address, to.address, from.origin).execute()
        return if (response.isSuccessful) {
            response.body()!!.toDto()
        } else {
            response.logErrorMessage("Error getting quotes.")
            null
        }
    }

    fun swap(from: TokenQuote, to: Token, allowPartialFill: Boolean, protocols: String, slippage: Double): SwapDto? {
        val response =
            oneInch.swap(
                chain.id,
                from.token.address,
                to.address,
                from.origin,
                myAddress,
                slippage,
                allowPartialFill,
                protocols
            ).execute()
        return if (response.isSuccessful) {
            response.body()!!.toDto()
        } else {
            response.logErrorMessage("Error getting quotes.")
            null
        }
    }

    private fun <T> Response<T>.logErrorMessage(info: String) {
        val text = this.errorBody()!!.charStream().readText()
        changeReteLimitWhenGetting429(this.code())
        getLogger().error("$info Response status: ${this.code()} $text")
    }

    private fun changeReteLimitWhenGetting429(code: Int) {
        if (code == 429) {
            if (!loweredLimit.get()) {
                loweredLimit.set(true)
                scope.launch {
                    getLogger().info("Lowering rate limit for 10 minutes.")
                    val defaultRps = settings.maxRps
                    settings.maxRps = 1
                    delay(600000)
                    settings.maxRps = defaultRps
                }
                loweredLimit.set(false)
            }
        }
    }
}



