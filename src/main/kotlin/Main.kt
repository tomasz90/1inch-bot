import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

fun main() {
    val oneInchClient = OneInchClient()
    repeat(10) {
        oneInchClient.getQuote(BSC_DAI, BSC_UST, AMOUNT_TO_SELL)
        oneInchClient.getQuote(BSC_DAI, BSC_USDT, AMOUNT_TO_SELL)
        oneInchClient.getQuote(BSC_DAI, BSC_USDC, AMOUNT_TO_SELL)
        oneInchClient.getQuote(BSC_DAI, BSC_TUSD, AMOUNT_TO_SELL)

        oneInchClient.getQuote(BSC_UST, BSC_DAI, AMOUNT_TO_SELL)
        oneInchClient.getQuote(BSC_UST, BSC_USDT, AMOUNT_TO_SELL)
        oneInchClient.getQuote(BSC_UST, BSC_USDC, AMOUNT_TO_SELL)
        oneInchClient.getQuote(BSC_UST, BSC_TUSD, AMOUNT_TO_SELL)

        oneInchClient.getQuote(BSC_USDT, BSC_UST, AMOUNT_TO_SELL)
        oneInchClient.getQuote(BSC_USDT, BSC_DAI, AMOUNT_TO_SELL)
        oneInchClient.getQuote(BSC_USDT, BSC_USDC, AMOUNT_TO_SELL)
        oneInchClient.getQuote(BSC_USDT, BSC_TUSD, AMOUNT_TO_SELL)

        oneInchClient.getQuote(BSC_TUSD, BSC_DAI, AMOUNT_TO_SELL)
        oneInchClient.getQuote(BSC_TUSD, BSC_USDT, AMOUNT_TO_SELL)
        oneInchClient.getQuote(BSC_TUSD, BSC_USDC, AMOUNT_TO_SELL)
        oneInchClient.getQuote(BSC_TUSD, BSC_UST, AMOUNT_TO_SELL)

        TimeUnit.SECONDS.sleep(5L)
    }
    exitProcess(0)
}