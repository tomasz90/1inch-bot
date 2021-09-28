package com.oneinch

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.api.blockchain.balance.IBalance
import com.oneinch.loader.Settings
import com.oneinch.requester.AbstractRequester
import com.oneinch.util.RateLimiter
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicBoolean

@Component
class Main(
    val mainCoroutine: CoroutineScope,
    val requester: AbstractRequester,
    val balance: IBalance,
    val chain: Chain,
    val settings: Settings,
    val isSwapping: AtomicBoolean,
    limiter: RateLimiter
) {

    private val pairs = createUniquePairs(chain.tokens, settings.excludedTokens)
    private val swapCoroutine = CoroutineScope(CoroutineName("swapCoroutine"))

    private val swap = limiter.decorateFunction { tokenQuote: TokenQuote, token: Token -> swap(tokenQuote, token) }

    fun run() {
        mainCoroutine.launch {
            if (mainCoroutine.isActive) {
                while (true) {
                    if (!isSwapping.get()) {
                        checkRatesForEveryPair(pairs)
                    }
                }
            }
        }
    }

    // todo: different advantages for different tokens???
    private fun checkRatesForEveryPair(pairs: List<Pair<Token, Token>>) {
        pairs.forEach { pair -> checkRatesForPair(pair) }
    }

    private fun checkRatesForPair(pair: Pair<Token, Token>) {
        when (val tokenQuote = balance.getERC20(pair.first)) {
            null -> { }
            else -> { swapWhenConditionsMet(tokenQuote, pair.second) }
        }
    }

    private fun swapWhenConditionsMet(tokenQuote: TokenQuote, token: Token) {
        if (tokenQuote.usdValue > settings.minSwapQuote) {
            val usdValue = balance.getERC20(token)?.usdValue
            val maxUsdShare = balance.getUsdValue() * settings.maximalTokenShare
            if (usdValue == null || usdValue <= maxUsdShare) {
                runBlocking { swap.invoke(tokenQuote, token) }
            }
        }
    }

    private suspend fun swap(tokenQuote: TokenQuote, token: Token) {
        swapCoroutine.launch { requester.swap(tokenQuote, token) }
    }

    private fun createUniquePairs(tokens: List<Token>, excluded: List<String>): List<Pair<Token, Token>> {
        return tokens.flatMap { token ->
            tokens.filter { diff -> diff != token }
                .filter { excl -> !excluded.contains(excl.symbol) }
                .map { Pair(token, it) }
        }
    }
}