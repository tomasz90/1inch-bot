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
import java.util.concurrent.atomic.AtomicBoolean

@Component
class Main(
    val requester: AbstractRequester,
    val balance: IBalance,
    val chain: Chain,
    val settings: Settings,
    val isSwapping: AtomicBoolean
) {

    val pairs = createUniquePairs(chain.tokens)
    val coroutine = CoroutineScope(CoroutineName("coroutine"))
    fun run() {
        isSwapping.set(false)
        runBlocking {
            while (true) {
                if (!isSwapping.get()) {
                    checkRatesForEveryPair(pairs, coroutine)
                }
                yield()
            }
        }
    }

    // TODO: 10.09.2021 add two counters rps, maybe second when too long time in one currency
    private suspend fun checkRatesForEveryPair(pairs: List<Pair<Token, Token>>, coroutine: CoroutineScope) {
        pairs.forEach { pair ->
            coroutine.launch { checkRatesForPair(pair) }
        }
        delay(200)
    }

    private suspend fun checkRatesForPair(pair: Pair<Token, Token>) {
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