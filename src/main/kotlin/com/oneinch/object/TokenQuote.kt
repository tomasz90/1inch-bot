package com.oneinch.`object`

import java.math.BigInteger

class CoinQuote(val coin: Token, origin: BigInteger) {

    var doubleValue: Double = -0.0

    init {
        doubleValue = calcDoubleValue(coin, origin)
    }
}

class TokenQuote(val token: Token, val origin: BigInteger) {

    var usdValue: Double = -0.0

    init {
        usdValue = calcDoubleValue(token, origin)
    }

    fun calcMinReturnAmountOfDifferentToken(differentToken: Token): BigInteger {
        val factor = differentToken.decimals.divide(token.decimals)
        return origin.toBigDecimal().multiply(factor).toBigInteger()
    }

    fun calcOrigin(quote: Double): BigInteger {
        val decimals = this.token.decimals
        return (quote.toBigDecimal() * decimals).toBigInteger()
    }
}

private fun calcDoubleValue(token: Token, origin: BigInteger): Double {
    val multiplication = token.decimals
    return origin.toBigDecimal().divide(multiplication).toDouble()
}