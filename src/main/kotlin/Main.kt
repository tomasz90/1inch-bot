import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

fun main() {

    getLogger().debug("Set waiting time in sec: ")
    val waitingTime = readLine()?.toLong()!!
    val oneInchClient = OneInchClient()
    getLogger().debug("Clean log? [y/n]")
    if (readLine().equals("y")) {
        withCleanLog(true)
    }

    while (true) {
        runBlocking {
            launch { oneInchClient.getQuote(BSC_DAI, BSC_UST, AMOUNT_TO_SELL) }
            launch { oneInchClient.getQuote(BSC_DAI, BSC_USDT, AMOUNT_TO_SELL) }
            launch { oneInchClient.getQuote(BSC_DAI, BSC_USDC, AMOUNT_TO_SELL) }
            launch { oneInchClient.getQuote(BSC_DAI, BSC_TUSD, AMOUNT_TO_SELL) }

            launch { oneInchClient.getQuote(BSC_UST, BSC_DAI, AMOUNT_TO_SELL) }
            launch { oneInchClient.getQuote(BSC_UST, BSC_USDT, AMOUNT_TO_SELL) }
            launch { oneInchClient.getQuote(BSC_UST, BSC_USDC, AMOUNT_TO_SELL) }
            launch { oneInchClient.getQuote(BSC_UST, BSC_TUSD, AMOUNT_TO_SELL) }

            launch { oneInchClient.getQuote(BSC_USDT, BSC_UST, AMOUNT_TO_SELL) }
            launch { oneInchClient.getQuote(BSC_USDT, BSC_DAI, AMOUNT_TO_SELL) }
            launch { oneInchClient.getQuote(BSC_USDT, BSC_USDC, AMOUNT_TO_SELL) }
            launch { oneInchClient.getQuote(BSC_USDT, BSC_TUSD, AMOUNT_TO_SELL) }

            launch { oneInchClient.getQuote(BSC_TUSD, BSC_DAI, AMOUNT_TO_SELL) }
            launch { oneInchClient.getQuote(BSC_TUSD, BSC_USDT, AMOUNT_TO_SELL) }
            launch { oneInchClient.getQuote(BSC_TUSD, BSC_USDC, AMOUNT_TO_SELL) }
            launch { oneInchClient.getQuote(BSC_TUSD, BSC_UST, AMOUNT_TO_SELL) }
        }
        getLogger().info(WAIT_MESSAGE)
        TimeUnit.SECONDS.sleep(waitingTime)
    }
}