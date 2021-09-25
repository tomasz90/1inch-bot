package com.oneinch.`object`

import java.math.BigInteger

class TokenQuote(val token: Token, val origin: BigInteger) {
    // TODO: 25.09.2021 TEST IT PROPERLY
    var usdValue: Double = 0.0

    init {
        usdValue = calcUSDValue()
    }

    fun calcMinReturnAmountOfDifferentToken(differentToken: Token): BigInteger {
        val factor = differentToken.decimals.toBigDecimal().divide(token.decimals.toBigDecimal())
        return origin.toBigDecimal().multiply(factor).toBigInteger()
    }

    private fun calcUSDValue(): Double {
        val multiplication = token.decimals.toBigDecimal()
        return origin.toBigDecimal().divide(multiplication).toDouble()
    }
}