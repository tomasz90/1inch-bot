package com.oneinch

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.config.Settings
import com.oneinch.on_chain_api.balance.IBalance
import com.oneinch.one_inch_api.requester.AbstractRequester
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

    fun run() {
        val pairs = createUniquePairs(chain.tokens)

        while (true) {
            checkRatesForEveryPair(pairs, chain, requester)
            getLogger().info("---------------- WAIT ----------------")
            TimeUnit.SECONDS.sleep(5)
        }
    }

    private fun checkRatesForEveryPair(pairs: List<Pair<Token, Token>>, chain: Chain, requester: AbstractRequester) {
        pairs.forEach { pair -> checkRatesForPair(pair, chain, requester) }
    }

    private fun checkRatesForPair(pair: Pair<Token, Token>, chain: Chain, requester: AbstractRequester) {
        when (val tokenQuote = balance.getERC20(pair.first)) {
            null -> { }
            else -> {
                if (tokenQuote.calcReadable(chain) > settings.minimalSwapQuote) {
                    requester.swap(chain.id, tokenQuote, pair.second)
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