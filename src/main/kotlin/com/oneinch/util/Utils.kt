package com.oneinch.util

import com.oneinch.`object`.TokenQuote
import com.oneinch.api.one_inch.api.data.Dto
import com.oneinch.loader.Settings
import com.oneinch.provider.advantage.IAdvantageProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import retrofit2.Response
import java.util.*

@Component
class Utils(val settings: Settings, val limiter: RateLimiter, val advantageProvider: IAdvantageProvider) {
    fun logRatesInfo(dto: Dto, percent: Double) {
        getLogger().info(
            "${dto.from.token.symbol}: ${dto.from.usdValue.precision()}, " +
                    "${dto.to.token.symbol}: ${dto.to.usdValue.precision()},  advantage: ${percent.precision()}," +
                    "  demandAdvantage: ${advantageProvider.advantage.precision()},  ${limiter.currentCalls} rps"
        )
    }

}

fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

fun Double.precision() = String.format("%.${2}f", this)

fun logSwapInfo(txHash: String, from: TokenQuote, to: TokenQuote) {
    getLogger().info(
        "SWAP: fromToken ${from.token.symbol}, fromAmount: ${from.usdValue.precision()}," +
                "toToken: ${to.token.symbol}, toAmount: ${to.usdValue.precision()}"
    )
    getLogger().info("---------------  WAITING FOR TRANSACTION SUCCEED  ---------------$")
    getLogger().info(txHash)
}

fun getDuration(date: Date): Double {
    val now = Date().time
    val duration = now - date.time
    return duration.toDouble() / 1000
}

fun <T> Response<T>.logErrorMessage(info: String) {
    val text = this.errorBody()!!.charStream().readText()
    getLogger().error("$info Response status: ${this.code()} $text")
}


