package com.oneinch

import com.oneinch.InputConfig.MINIMAL_SWAP_QUOTE
import com.oneinch.common.Chain
import com.oneinch.common.WAIT_MESSAGE
import com.oneinch.on_chain_api.IBalance
import com.oneinch.oneinch_api.AbstractRequester
import com.oneinch.oneinch_api.api.data.TokenQuote
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
            getLogger().error(exception.message)
        }

        while (true) {
            checkRatesForEveryPair(chain, requester, handler)
            getLogger().info(WAIT_MESSAGE)
            TimeUnit.SECONDS.sleep(5)
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
                            val availableBalance = balance.getERC20(token.address)
                            val tokenQuote = TokenQuote(token, availableBalance)
//                            if (tokenQuote.readable < MINIMAL_SWAP_QUOTE) {
//                                return@launch
//                            }
                            requester.swap(chain.id, tokenQuote, diffToken)
                        }
                    }
                }
        }
    }
}