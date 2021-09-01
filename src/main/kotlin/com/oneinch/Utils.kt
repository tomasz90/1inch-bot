import com.oneinch.InputConfig.LOG_DECIMAL_PRECISION
import org.apache.log4j.FileAppender
import org.apache.log4j.LogManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import com.oneinch.quote_request.api.data.TokenQuote

fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

fun cleanLog(boolean: Boolean) {
    if (boolean) {
        val appender: FileAppender = LogManager.getRootLogger().getAppender("FILE") as FileAppender
        appender.setFile("log.out", false, false, DEFAULT_BUFFER_SIZE)
    }
}

fun logRatesInfo(from: TokenQuote, to: TokenQuote, percent: Double) {
    getLogger().info(
        "${from.token.symbol}: ${from.readable.precision()}, " +
                "${to.token.symbol}: ${to.readable.precision()},  advantage: ${percent.precision(2)}"
    )
}

fun logSwapInfo(from: TokenQuote, to: TokenQuote) {
    getLogger().info(
        "SWAP: fromToken ${from.token.symbol}, fromAmount: ${from.readable}," +
                "toToken: ${to.token.symbol}, toAmount: ${to.readable}"
    )
}

fun calculateAdvantage(from: TokenQuote, to: TokenQuote): Double {
    return (to.readable - from.readable) / from.readable * 100
}

fun Double.precision(int: Int = LOG_DECIMAL_PRECISION) = String.format("%.${int}f", this)


