package com.oneinch

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.config.Settings
import com.oneinch.on_chain_api.balance.IBalance
import com.oneinch.one_inch_api.requester.AbstractRequester
import com.oneinch.util.RateLimiter
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicBoolean

@Component
class Main(
    val requester: AbstractRequester,
    val balance: IBalance,
    val chain: Chain,
    val settings: Settings,
    val isSwapping: AtomicBoolean,
    limiter: RateLimiter
) {

    private val pairs = createUniquePairs(chain.tokens)
    private val coroutine = CoroutineScope(CoroutineName("coroutine"))
    private val swap = limiter.decorateFunction { tokenQuote: TokenQuote, token: Token -> swap(tokenQuote, token) }

    fun run() {
        while (true) {
            if (!isSwapping.get()) {
                checkRatesForEveryPair(pairs)
            }
        }
    }

    // TODO: 10.09.2021 add counter when too long time in one currency
    // todo: different advantages for different tokens???
    // todo: increase gas fees dynamically when tx is very profitable
    private fun checkRatesForEveryPair(pairs: List<Pair<Token, Token>>) {
        pairs.forEach { pair -> checkRatesForPair(pair) }
    }

    private fun checkRatesForPair(pair: Pair<Token, Token>) {
        when (val tokenQuote = balance.getERC20(pair.first)) {
            null -> { }
            else -> { swapIfMinimalBalance(tokenQuote, pair.second) }
        }
    }

    private fun swapIfMinimalBalance(tokenQuote: TokenQuote, token: Token) {
        if (tokenQuote.calcReadable(chain) > settings.minimalSwapQuote) {
            runBlocking { swap.invoke(tokenQuote, token) }
        }
    }

    private suspend fun swap(tokenQuote: TokenQuote, token: Token) {
        coroutine.launch { requester.swap(tokenQuote, token) }
    }

    private fun createUniquePairs(tokens: List<Token>): List<Pair<Token, Token>> {
        return tokens.flatMap { token ->
            tokens.filter { diff -> diff != token }
                .map { Pair(it, token) }
        }
    }
}