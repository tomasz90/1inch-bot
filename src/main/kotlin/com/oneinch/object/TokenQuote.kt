package com.oneinch.`object`

import java.math.BigInteger
import kotlin.math.pow

class TokenQuote(val symbol: String, val address: String, val origin: BigInteger) {

    fun calcReadable(chain: Chain): Double {
        val decimals = chain.tokens.first { it.address ==  this.address}.decimals
        val multiplication = 10.0.pow(decimals)
        return origin.toBigDecimal().toDouble()/multiplication
    }
}