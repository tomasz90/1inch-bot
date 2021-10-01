package unit

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.loader.Settings
import java.util.concurrent.ThreadLocalRandom

class MainTest(val settings: Settings) {

    fun swapOnlyToMaximalShare(tokenQuote: TokenQuote, token: Token, tokenShare: Double?, maxUsdShare: Double): TokenQuote? {
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
            return TokenQuote(tokenQuote.token, origin)
        }
        return null
    }

    private fun generateRandom(min: Double, max: Double): Double = ThreadLocalRandom.current().nextDouble(min, max)

}