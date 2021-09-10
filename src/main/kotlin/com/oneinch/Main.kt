package com.oneinch

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.config.Settings
import com.oneinch.on_chain_api.balance.Balance
import com.oneinch.on_chain_api.balance.IBalance
import com.oneinch.one_inch_api.requester.AbstractRequester
import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RateLimiterConfig
import io.github.resilience4j.ratelimiter.RateLimiterRegistry
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.TimeUnit

@Component
class Main(val requester: AbstractRequester, val balance: IBalance, val chain: Chain, val settings: Settings) {
    fun run() {
        val pairs = createUniquePairs(chain.tokens)
        runBlocking {
            while (true) {
                var coroutine = CoroutineScope(CoroutineName("coroutine"))
                checkRatesForEveryPair(pairs, coroutine)
            }
        }
    }

    private suspend fun checkRatesForEveryPair(pairs: List<Pair<Token, Token>>, coroutine: CoroutineScope) {
        pairs.forEach { pair ->
            coroutine.launch { checkRatesForPair(pair, coroutine) }
        }
        delay(200)
    }

    private suspend fun checkRatesForPair(pair: Pair<Token, Token>, coroutine: CoroutineScope) {
        when (val tokenQuote = balance.getERC20(pair.first)) {
            null -> {
            }
            else -> {
                if (tokenQuote.calcReadable(chain) > settings.minimalSwapQuote) {
                    requester.swap(tokenQuote, pair.second, coroutine)
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