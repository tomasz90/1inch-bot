package com.oneinch.util

import com.oneinch.`object`.TokenQuote
import com.oneinch.config.Settings
import com.oneinch.one_inch_api.api.data.Dto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

@Component
class Utils(val settings: Settings, val limiter: RateLimiter) {

    fun logRatesInfo(dto: Dto, percent: Double) {
        getLogger().info(
            "${dto.from.token.symbol}: ${dto.from.calcReadable().precision()}, " +
                    "${dto.to.token.symbol}: ${dto.to.calcReadable().precision()},  advantage: ${percent.precision(2)}," +
                    "  demandAdvantage: ${settings.advantage.precision(2)},  ${limiter.currentCalls} rps"
        )
    }

    fun logSwapInfo(txHash: String, from: TokenQuote, to: TokenQuote) {
        getLogger().info(
            "SWAP: fromToken ${from.token.symbol}, fromAmount: ${from.calcReadable().precision()}," +
                    "toToken: ${to.token.symbol}, toAmount: ${to.calcReadable().precision()}"
        )
        getLogger().info("---------------  WAITING FOR TRANSACTION SUCCEED  ---------------\n$txHash")
    }

    fun Double.precision(int: Int? = settings.logDecimalPrecision) = String.format("%.${int}f", this)

}

fun calculateAdvantage(dto: Dto): Double {
    return (dto.to.calcReadable() - dto.from.calcReadable()) / dto.from.calcReadable() * 100
}


