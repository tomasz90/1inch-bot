package com.oneinch.util

import com.oneinch.`object`.Chain
import com.oneinch.`object`.TokenQuote
import com.oneinch.config.Settings
import org.apache.log4j.FileAppender
import org.apache.log4j.LogManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

@Component
class Utils(val chain: Chain, val settings: Settings, val limiter: RateLimiter) {

    fun logRatesInfo(from: TokenQuote, to: TokenQuote, percent: Double, demandAdvantage: Double) {
        getLogger().info(
            "${from.symbol}: ${from.calcReadable(chain).precision()}, " +
                    "${to.symbol}: ${to.calcReadable(chain).precision()},  advantage: ${percent.precision(2)}," +
                    "  demandAdvantage: ${demandAdvantage.precision(2)},  ${limiter.currentCalls} rps"
        )
    }

    fun logSwapInfo(from: TokenQuote, to: TokenQuote) {
        getLogger().info(
            "SWAP: fromToken ${from.symbol}, fromAmount: ${from.calcReadable(chain).precision()}," +
                    "toToken: ${to.symbol}, toAmount: ${to.calcReadable(chain).precision()}"
        )
    }

    fun calculateAdvantage(from: TokenQuote, to: TokenQuote): Double {
        return (to.calcReadable(chain) - from.calcReadable(chain)) / from.calcReadable(chain) * 100
    }

    fun Double.precision(int: Int? = settings.logDecimalPrecision) = String.format("%.${int}f", this)
}


