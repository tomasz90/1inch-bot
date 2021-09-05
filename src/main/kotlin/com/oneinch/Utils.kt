import Const.precision
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import org.apache.log4j.FileAppender
import org.apache.log4j.LogManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

fun cleanLog(boolean: Boolean) {
    if (boolean) {
        val appender: FileAppender = LogManager.getRootLogger().getAppender("FILE") as FileAppender
        appender.setFile("log.out", false, false, DEFAULT_BUFFER_SIZE)
    }
}

fun logRatesInfo(from: TokenQuote, to: TokenQuote, percent: Double) {
    getLogger().info(
        "${from.symbol}: ${from.readable.precision()}, " +
                "${to.symbol}: ${to.readable.precision()},  advantage: ${percent.precision(2)}"
    )
}

fun logSwapInfo(from: TokenQuote, to: TokenQuote) {
    getLogger().info(
        "SWAP: fromToken ${from.symbol}, fromAmount: ${from.readable.precision()}," +
                "toToken: ${to.symbol}, toAmount: ${to.readable.precision()}"
    )
}

fun calculateAdvantage(from: TokenQuote, to: TokenQuote): Double {
    return (to.readable-from.readable)/from.readable * 100
}

fun Double.precision(int: Int? = precision) = String.format("%.${int}f", this)

object Const {
    lateinit var tokens: List<Token>
    var precision: Int? = null
}


