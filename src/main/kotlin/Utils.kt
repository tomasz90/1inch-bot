import Config.LOG_DECIMAL_PRECISION
import org.apache.log4j.FileAppender
import org.apache.log4j.LogManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import quote_request.SwapQuoteResponse

fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

fun withCleanLog(boolean: Boolean) {
    if (boolean) {
        val appender: FileAppender = LogManager.getRootLogger().getAppender("FILE") as FileAppender
        appender.setFile("log.out", false, false, DEFAULT_BUFFER_SIZE)
    }
}

fun calculateAdvantage(response: SwapQuoteResponse): Double {
    return (response.to.readable - response.from.readable) / response.from.readable * 100
}

fun logRatesInfo(response: SwapQuoteResponse, percent: Double) {
    getLogger().info(
        "${response.from.symbol}: ${response.from.readable.precision()}, " +
                "${response.to.symbol}: ${response.to.readable.precision()},  advantage: ${percent.precision(2)}"
    )
}

fun logSwapInfo(response: SwapQuoteResponse) {
    getLogger().info(
        "SWAP: fromToken ${response.from.symbol}, fromAmount: ${response.from.readable}," +
                "toToken: ${response.to.symbol}, toAmount: ${response.to.readable}"
    )
}

fun Double.precision(int: Int = LOG_DECIMAL_PRECISION) = String.format("%.${int}f", this)


