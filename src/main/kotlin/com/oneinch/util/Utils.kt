package com.oneinch.util

import com.oneinch.`object`.TokenQuote
import com.oneinch.config.Settings
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

@Component
class Utils(val settings: Settings, val limiter: RateLimiter) {

    fun logRatesInfo(from: TokenQuote, to: TokenQuote, percent: Double, demandAdvantage: Double) {
        getLogger().info(
            "${from.token.symbol}: ${from.calcReadable().precision()}, " +
                    "${to.token.symbol}: ${to.calcReadable().precision()},  advantage: ${percent.precision(2)}," +
                    "  demandAdvantage: ${demandAdvantage.precision(2)},  ${limiter.currentCalls} rps"
        )
    }

    fun logSwapInfo(from: TokenQuote, to: TokenQuote) {
        getLogger().info(
            "SWAP: fromToken ${from.token.symbol}, fromAmount: ${from.calcReadable().precision()}," +
                    "toToken: ${to.token.symbol}, toAmount: ${to.calcReadable().precision()}"
        )
    }

    fun calculateAdvantage(from: TokenQuote, to: TokenQuote): Double {
        return (to.calcReadable() - from.calcReadable()) / from.calcReadable() * 100
    }

    fun Double.precision(int: Int? = settings.logDecimalPrecision) = String.format("%.${int}f", this)
}


