package com.oneinch

import com.oneinch.`object`.Chain
import com.oneinch.config.Settings
import com.oneinch.on_chain_api.balance.IBalance
import com.oneinch.one_inch_api.requester.AbstractRequester
import getLogger
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class Main {

    @Autowired
    private lateinit var requester: AbstractRequester

    @Autowired
    private lateinit var balance: IBalance

    @Autowired
    private lateinit var chain: Chain

    @Autowired
    private lateinit var settings: Settings

    @DelicateCoroutinesApi
    fun run() {
//    getLogger().debug("Set waiting time in sec: ")
//    val waitingTime = readLine()?.toLong()!!
//
//    getLogger().debug("Clean log? [y/n]")
//    if (readLine().equals("y")) {
//        withCleanLog(true)
//    }

        val handler = CoroutineExceptionHandler { _, exception ->
            getLogger().error(exception.message)
        }

        while (true) {
            checkRatesForEveryPair(chain, requester, handler)
            getLogger().info("---------------- WAIT ----------------")
            TimeUnit.SECONDS.sleep(20)
        }
    }

    @DelicateCoroutinesApi
    private fun checkRatesForEveryPair(chain: Chain, requester: AbstractRequester, handler: CoroutineExceptionHandler) {
        val tokens = chain.tokens
        tokens.forEach { token ->
            tokens.filter { diffToken -> diffToken != token }
                .forEach { diffToken ->
                    runBlocking {
                        GlobalScope.launch(handler) {
                            val availableTokenQuote = balance.getERC20(token)
//                            if (availableTokenQuote.readable < settings.minimalSwapQuote) {
//                                return@launch
//                            }
                            requester.swap(chain.id, availableTokenQuote, diffToken)
                        }
                    }
                }
        }
    }
}