package com.oneinch.util

import com.oneinch.`object`.TokenQuote
import com.oneinch.config.Settings
import com.oneinch.api.one_inch.api.data.Dto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

@Component
class Utils(val settings: Settings, val limiter: RateLimiter) {

    fun logRatesInfo(dto: Dto, percent: Double) {
        getLogger().info(
            "${dto.from.token.symbol}: ${dto.from.calcReadable().precision()}, " +
                    "${dto.to.token.symbol}: ${dto.to.calcReadable().precision()},  advantage: ${percent.precision()}," +
                    "  demandAdvantage: ${settings.minAdvantage.precision()},  ${limiter.currentCalls} rps"
        )
    }

}

fun Double.precision() = String.format("%.${2}f", this)

fun logSwapInfo(txHash: String, from: TokenQuote, to: TokenQuote) {
    getLogger().info(
        "SWAP: fromToken ${from.token.symbol}, fromAmount: ${from.calcReadable().precision()}," +
                "toToken: ${to.token.symbol}, toAmount: ${to.calcReadable().precision()}"
    )
    getLogger().info("---------------  WAITING FOR TRANSACTION SUCCEED  ---------------\n$txHash")
}

fun getDuration(date: Date): Double {
    val now = Date().time
    val duration = now - date.time
    return duration.toDouble() / 1000
}


