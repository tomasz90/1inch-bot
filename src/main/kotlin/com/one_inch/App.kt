package com.one_inch

import WAIT_MESSAGE
import com.one_inch.common.Chain
import com.one_inch.quote_request.OneInchClient
import com.one_inch.quote_request.api.data.TokenQuote
import getLogger
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.concurrent.TimeUnit

@SpringBootApplication
open class DemoApplication: CommandLineRunner {

    @Autowired
    lateinit var oneInchClient: OneInchClient

    @DelicateCoroutinesApi
    override fun run(vararg args: String?) {
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
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}


@DelicateCoroutinesApi
fun checkRatesForEveryPair(chain: Chain, oneInchClient: OneInchClient, handler: CoroutineExceptionHandler) {
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