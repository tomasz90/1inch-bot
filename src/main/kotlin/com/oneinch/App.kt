package com.oneinch

import com.oneinch.common.Chain
import com.oneinch.common.WAIT_MESSAGE
import com.oneinch.oneinch_api.OneInchClient
import com.oneinch.oneinch_api.api.data.TokenQuote
import getLogger
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.StandardEnvironment
import java.util.concurrent.TimeUnit

@SpringBootApplication
open class App : CommandLineRunner {

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
    val environment: ConfigurableEnvironment = StandardEnvironment()
    environment.setActiveProfiles("realAccount")
    val application = SpringApplication(App::class.java)
    application.setEnvironment(environment)
    application.run(*args)
    runApplication<App>(*args)
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