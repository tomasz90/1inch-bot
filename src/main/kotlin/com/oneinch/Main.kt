package com.oneinch

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.config.Settings
import com.oneinch.on_chain_api.balance.IBalance
import com.oneinch.one_inch_api.requester.AbstractRequester
import getLogger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import toReadable
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

    fun run() {
//    getLogger().debug("Set waiting time in sec: ")
//    val waitingTime = readLine()?.toLong()!!
//
//    getLogger().debug("Clean log? [y/n]")
//    if (readLine().equals("y")) {
//        withCleanLog(true)
//    }
        val pairs = createUniquePairs(chain.tokens)

        while (true) {
            checkRatesForEveryPair(pairs, chain, requester)
            getLogger().info("---------------- WAIT ----------------")
            TimeUnit.SECONDS.sleep(5)
        }
    }

    private fun checkRatesForEveryPair(pairs: List<Pair<Token, Token>>, chain: Chain, requester: AbstractRequester) {
        pairs.forEach { pair ->
            when (val tokenQuote = balance.getERC20(pair.first)) {
                null -> { }
                else -> {
                    println("check pair: ${pair.first.symbol}   ${pair.second.symbol}")
                    if (tokenQuote.toReadable() > settings.minimalSwapQuote) {
                        requester.swap(chain.id, tokenQuote, pair.second)
                    }
                }
            }
        }
    }


    private fun createUniquePairs(tokens: List<Token>): List<Pair<Token, Token>> {
        return tokens.flatMap { token ->
            tokens.filter { diff -> diff != token }
                .map { Pair(it, token) }
        }
    }
}