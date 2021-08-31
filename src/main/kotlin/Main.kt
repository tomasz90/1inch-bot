import Config.AMOUNT_TO_SELL
import Config.CHAIN
import common.Chain
import kotlinx.coroutines.*
import quote_request.OneInchClient
import quote_request.api.data.TokenQuote
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
        TimeUnit.SECONDS.sleep(5)
    }
}

@DelicateCoroutinesApi
fun checkRatesForEveryPair(chain: Chain, oneInchClient: OneInchClient, handler: CoroutineExceptionHandler) {
    val tokens = chain.tokens
    tokens.forEach { token ->
        tokens.filter { diffToken -> diffToken != token }
            .forEach { diffToken ->
                runBlocking {
                    val tokenQuote = TokenQuote(token, AMOUNT_TO_SELL)
                    GlobalScope.launch(handler) { oneInchClient.getQuote(chain.id, tokenQuote, diffToken) }
                }
            }
    }
}