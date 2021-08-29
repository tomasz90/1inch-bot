import org.apache.log4j.FileAppender
import org.apache.log4j.LogManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import quote_request.QuoteResponse
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.pow

fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

fun withCleanLog(boolean: Boolean) {
    if (boolean) {
        val appender: FileAppender = LogManager.getRootLogger().getAppender("FILE") as FileAppender
        appender.setFile("log.out", false, false, DEFAULT_BUFFER_SIZE)
    }
}

fun calculateAdvantage(response: QuoteResponse): Float {
    val fromFloat = cut(response.fromTokenAmount, response.fromToken.decimals).toFloat()
    val toFloat = cut(response.toTokenAmount, response.toToken.decimals).toFloat()
    return (toFloat - fromFloat) / fromFloat * 100
}

fun logRatesInfo(response: QuoteResponse, percent: Float) {
    val fromDec = cut(response.fromTokenAmount, response.fromToken.decimals)
    val toDec = cut(response.toTokenAmount, response.toToken.decimals)
    getLogger()
        .info(
            "${response.fromToken.symbol}: $fromDec, ${response.toToken.symbol}: $toDec," +
                    "   advantage: ${String.format("%.2f", percent)}%"
        )
}

// adjusts quotes to token standard, from being more readable
fun expand(quote: Double, decimals: Int): BigInteger {
    return BigDecimal.valueOf(quote).multiply(BigInteger.valueOf(10L).pow(decimals).toBigDecimal()).toBigInteger()
}
// adjust quotes to be more readable
fun cut(quote: BigInteger, decimals: Int): Long {
    return (quote/BigInteger.valueOf(10).pow(decimals)).toLong()
}


