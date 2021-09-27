package com.oneinch.`object`

import java.math.BigInteger

open class CoinQuote(open val token: Coin, open val origin: BigInteger) {

    internal fun calcDoubleValue(): Double {
        val multiplication = token.decimals
        return origin.toBigDecimal().divide(multiplication).toDouble()
    }
}

class TokenQuote(override val token: Token, override val origin: BigInteger): CoinQuote(token, origin) {

    var usdValue: Double = -0.0

    init {
        usdValue = calcDoubleValue()
    }

    fun calcMinReturnAmountOfDifferentToken(differentToken: Token): BigInteger {
        val factor = differentToken.decimals.divide(token.decimals)
        return origin.toBigDecimal().multiply(factor).toBigInteger()
    }
}