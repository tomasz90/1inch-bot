import org.apache.log4j.FileAppender
import org.apache.log4j.LogManager
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import quote_request.Token

fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

fun withCleanLog(boolean: Boolean) {
    if (boolean) {
        val appender: FileAppender = LogManager.getRootLogger().getAppender("FILE") as FileAppender
        appender.setFile("log.out", false, false, DEFAULT_BUFFER_SIZE)
    }
}

fun String.addDecimals(int: Int): String {
    var decimals = ""
    repeat(int) {
        decimals += "0"
    }
    return this + decimals
}

fun String.removeDecimals(int: Int) = this.subSequence(0, this.length - int)

fun calculateAdvantage(value1: String, value2: String) =
    (value2.toDouble() - value1.toDouble()) / value1.toDouble() * 100

fun logRatesInfo(from: Token, to: Token, fromQuote: String, toQuote: String, percent: Double, isOpportunity: String) {
    getLogger()
        .info("${from.name}: $fromQuote, ${to.name}: $toQuote," +
                "   advantage: ${String.format("%.2f", percent)}%    $isOpportunity")
}

fun checkOpportunity(percent: Double): String {
    var isOpportunity = ""
    if (percent > 0.5) {
        isOpportunity = "Opportunity !!!"
    }
    return isOpportunity
}


