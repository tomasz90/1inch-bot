package com.oneinch

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.api.blockchain.balance.Balance
import com.oneinch.loader.Settings
import com.oneinch.requester.Requester
import com.oneinch.util.RateLimiter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import org.springframework.stereotype.Component
import java.util.concurrent.ThreadLocalRandom

@Component
class Main(
    val scope: CoroutineScope,
    val requester: Requester,
    val balance: Balance,
    val chain: Chain,
    val settings: Settings,
    val isSwapping: Mutex,
    limiter: RateLimiter
) {

    private val pairs = createUniquePairs(chain.tokens, settings.excludedTokens)
    private val swapCoroutine = CoroutineScope(scope.coroutineContext + Dispatchers.IO)

    private val swap = limiter.decorateFunction { tokenQuote: TokenQuote, token: Token -> swap(tokenQuote, token) }

    fun run() {
        scope.launch {
            if (scope.isActive) {
                while (true) {
                    if (!isSwapping.isLocked) {
                        checkRatesForEveryPair(pairs)
                    }
                }
            }
        }
    }

    private fun checkRatesForEveryPair(pairs: List<Pair<Token, Token>>) {
        pairs.forEach { pair -> checkRatesForPair(pair) }
    }

    private fun checkRatesForPair(pair: Pair<Token, Token>) {
        when (val tokenQuote = balance.getERC20(pair.first)) {
            null -> { }
            else -> { swapWhenMoreThanMinimalQuote(tokenQuote, pair.second) }
        }
    }

    private fun swapWhenMoreThanMinimalQuote(tokenQuote: TokenQuote, token: Token) {
        if (tokenQuote.usdValue > settings.minSwapQuote) {
            swapWhenNotExceedingMaximalShare(tokenQuote, token)
        }
    }

    private fun swapWhenNotExceedingMaximalShare(tokenQuote: TokenQuote, token: Token) {
        val tokenShare = balance.getERC20(token)?.usdValue
        val maxUsdShare = balance.getUsdValue() * settings.maximalTokenShare
        if (tokenShare == null || tokenShare <= maxUsdShare) {
            swapOnlyToMaximalShare(tokenQuote, token, tokenShare, maxUsdShare)
        }
    }

    // TODO: Any changes here put in corresponding class in tests
    private fun swapOnlyToMaximalShare(tokenQuote: TokenQuote, token: Token, tokenShare: Double?, maxUsdShare: Double) {
        var swapValue =
            if (tokenShare == null || maxUsdShare - tokenShare >= tokenQuote.usdValue) {
                if (tokenQuote.usdValue <= maxUsdShare) {
                    tokenQuote.usdValue
                } else {
                    maxUsdShare
                }
            } else {
                maxUsdShare - tokenShare
            }
        if (swapValue > settings.minSwapQuote) {
            swapValue = if(!settings.randomSwapQuote) swapValue else generateRandom(settings.minSwapQuote, swapValue)
            val origin = tokenQuote.calcOrigin(swapValue)
            val tokenQuoteToSwap = TokenQuote(tokenQuote.token, origin)
            runBlocking { swap.invoke(tokenQuoteToSwap, token) }
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

    private fun generateRandom(min: Double, max: Double): Double = ThreadLocalRandom.current().nextDouble(min, max)
}