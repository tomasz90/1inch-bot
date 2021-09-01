package com.oneinch

import com.oneinch.common.Chain
import com.oneinch.common.WAIT_MESSAGE
import com.oneinch.oneinch_api.OneInchClient
import com.oneinch.oneinch_api.api.data.TokenQuote
import getLogger
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class Main {

    @Autowired
    private lateinit var oneInchClient: OneInchClient

    @DelicateCoroutinesApi
    fun run() {
        val chain: Chain = InputConfig.CHAIN

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
    private fun checkRatesForEveryPair(chain: Chain, oneInchClient: OneInchClient, handler: CoroutineExceptionHandler) {
        val tokens = chain.tokens
        tokens.forEach { token ->
            tokens.filter { diffToken -> diffToken != token }
                .forEach { diffToken ->
                    runBlocking {
                        val tokenQuote = TokenQuote(token, InputConfig.AMOUNT_TO_SELL)
                        GlobalScope.launch(handler) { oneInchClient.getQuote(chain.id, tokenQuote, diffToken) }
                    }
                }
        }
    }
}