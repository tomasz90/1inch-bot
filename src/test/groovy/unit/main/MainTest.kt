package unit.main

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote

class MainTest {
    companion object {
        fun swapOnlyToMaximalShare(tokenQuote: TokenQuote, token: Token, tokenShare: Double?, maxUsdShare: Double)
                : TokenQuote {
            val tokenQuoteToSwap =
                if (tokenShare == null || maxUsdShare - tokenShare >= tokenQuote.usdValue) {
                    if (tokenQuote.usdValue <= maxUsdShare) {
                        tokenQuote
                    } else {
                        val origin = tokenQuote.calcOrigin(maxUsdShare)
                        TokenQuote(tokenQuote.token, origin)
                    }
                } else {
                    val swapValue = maxUsdShare - tokenShare
                    val origin = tokenQuote.calcOrigin(swapValue)
                    TokenQuote(tokenQuote.token, origin)
                }
            return tokenQuoteToSwap
        }
    }
}