import Const.precision
import Const.tokens
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.repository.dao.FakeTokenQuoteEntity
import org.apache.log4j.FileAppender
import org.apache.log4j.LogManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigInteger
import kotlin.math.pow

fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

fun cleanLog(boolean: Boolean) {
    if (boolean) {
        val appender: FileAppender = LogManager.getRootLogger().getAppender("FILE") as FileAppender
        appender.setFile("log.out", false, false, DEFAULT_BUFFER_SIZE)
    }
}

fun logRatesInfo(from: TokenQuote, to: TokenQuote, percent: Double) {
    getLogger().info(
        "${from.symbol}: ${from.toReadable().precision()}, " +
                "${to.symbol}: ${to.toReadable().precision()},  advantage: ${percent.precision(2)}"
    )
}

fun logSwapInfo(from: TokenQuote, to: TokenQuote) {
    getLogger().info(
        "SWAP: fromToken ${from.symbol}, fromAmount: ${from.toReadable().precision()}," +
                "toToken: ${to.symbol}, toAmount: ${to.toReadable().precision()}"
    )
}

fun TokenQuote.toReadable(): Double {
    val double = origin.toDouble()
    val multiplication = calculateMultiplication(address)
    return double/multiplication
}

fun FakeTokenQuoteEntity.toOrigin(): BigInteger {
    val multiplication = calculateMultiplication(address)
    return (readable*multiplication).toBigDecimal().toBigInteger()
}

fun calculateMultiplication(address: String): Double {
    val decimals = tokens.first { it.address == address }.decimals
    return 10.0.pow(decimals.toDouble())
}

fun calculateAdvantage(from: TokenQuote, to: TokenQuote): Double {
    return (to.origin.toDouble() - from.origin.toDouble()) / from.origin.toDouble() * 100
}

fun Double.precision(int: Int? = precision) = String.format("%.${int}f", this)

object Const {
    lateinit var tokens: List<Token>
    var precision: Int? = null
}


