package com.oneinch

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.config.Settings
import com.oneinch.on_chain_api.balance.Balance
import com.oneinch.on_chain_api.balance.IBalance
import com.oneinch.one_inch_api.requester.AbstractRequester
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class Main(val requester: AbstractRequester, val balance: IBalance, val chain: Chain, val settings: Settings) {

    val pairs = createUniquePairs(chain.tokens)

    fun run() {
        while (true) {
            checkRatesForEveryPair(pairs)
            getLogger().info("---------------- WAIT ----------------")
            TimeUnit.SECONDS.sleep(settings.sleepTime)
        }
    }

    private fun checkRatesForEveryPair(pairs: List<Pair<Token, Token>>) {
        pairs.forEach { pair -> checkRatesForPair(pair) }
    }

    private fun checkRatesForPair(pair: Pair<Token, Token>) {
        when (val tokenQuote = balance.getERC20(pair.first)) {
            null -> { }
            else -> {
                if (tokenQuote.calcReadable(chain) > settings.minimalSwapQuote) {
                    requester.swap(tokenQuote, pair.second)
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