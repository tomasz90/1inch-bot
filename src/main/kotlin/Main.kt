import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

fun main() {
    val oneInchClient = OneInchClient()
    repeat(10) {
        oneInchClient.getQuote(BSC_DAI, BSC_UST, AMOUNT_TO_SELL)
        TimeUnit.SECONDS.sleep(5L)
    }
    exitProcess(0)
}