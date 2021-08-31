import Config.LOG_DECIMAL_PRECISION
import org.apache.log4j.FileAppender
import org.apache.log4j.LogManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import quote_request.api.data.Token

fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

fun cleanLog(boolean: Boolean) {
    if (boolean) {
        val appender: FileAppender = LogManager.getRootLogger().getAppender("FILE") as FileAppender
        appender.setFile("log.out", false, false, DEFAULT_BUFFER_SIZE)
    }
}

fun logRatesInfo(from: Token, to: Token, percent: Double) {
    getLogger().info(
        "${from.symbol}: ${from.readable.precision()}, " +
                "${to.symbol}: ${to.readable.precision()},  advantage: ${percent.precision(2)}"
    )
}

fun logSwapInfo(from: Token, to: Token) {
    getLogger().info(
        "SWAP: fromToken ${from.symbol}, fromAmount: ${from.readable}," +
                "toToken: ${to.symbol}, toAmount: ${to.readable}"
    )
}

fun calculateAdvantage(from: Token, to: Token): Double {
    return (to.readable - from.readable) / from.readable * 100
}

fun Double.precision(int: Int = LOG_DECIMAL_PRECISION) = String.format("%.${int}f", this)


