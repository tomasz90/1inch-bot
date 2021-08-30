import Config.AMOUNT_TO_SELL
import Config.CHAIN
import kotlinx.coroutines.*
import quote_request.OneInchClient
import java.util.concurrent.TimeUnit

@DelicateCoroutinesApi
fun main() {

    val chain: Chain = CHAIN
    val oneInchClient = OneInchClient()

//    getLogger().debug("Set waiting time in sec: ")
//    val waitingTime = readLine()?.toLong()!!
//
//    getLogger().debug("Clean log? [y/n]")
//    if (readLine().equals("y")) {
//        withCleanLog(true)
//    }

    val handler = CoroutineExceptionHandler { _, exception ->
        getLogger().error("Error, $exception")
    }

    while (true) {
        checkRatesForEveryPair(chain, oneInchClient, handler)
        getLogger().info(WAIT_MESSAGE)
        TimeUnit.SECONDS.sleep(10)
    }
}

@DelicateCoroutinesApi
fun checkRatesForEveryPair(chain: Chain, oneInchClient: OneInchClient, handler: CoroutineExceptionHandler) {
    val tokens = chain.tokens
    tokens.forEach { token ->
        tokens.filter { diffToken -> diffToken != token }
            .forEach { diffToken ->
                runBlocking {
                    val amount = Amount(AMOUNT_TO_SELL, token.decimals)
                    GlobalScope.launch(handler) { oneInchClient.swap(chain.id, token.address, diffToken.address, amount) }
                }
            }
    }
}