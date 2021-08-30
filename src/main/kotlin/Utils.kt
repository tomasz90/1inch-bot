import Config.LOG_DECIMAL_PRECISION
import org.apache.log4j.FileAppender
import org.apache.log4j.LogManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import quote_request.SwapResponse

fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

fun withCleanLog(boolean: Boolean) {
    if (boolean) {
        val appender: FileAppender = LogManager.getRootLogger().getAppender("FILE") as FileAppender
        appender.setFile("log.out", false, false, DEFAULT_BUFFER_SIZE)
    }
}

fun calculateAdvantage(from: Amount, to: Amount): Double {
    return (to.readable!! - from.readable!!) / from.readable!! * 100
}

fun logRatesInfo(response: SwapResponse, percent: Double, from: Amount, to: Amount) {
    getLogger().info(
        "${response.fromToken.symbol}: ${from.readable!!.precision()}, " +
                "${response.toToken.symbol}: ${to.readable!!.precision()},  advantage: ${percent.precision(2)}"
    )
}

fun Double.precision(int: Int = LOG_DECIMAL_PRECISION) = String.format("%.${int}f", this)


