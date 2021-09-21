package com.oneinch.`object`

import java.math.BigInteger
import kotlin.math.pow

class TokenQuote(val token: Token, val origin: BigInteger) {

    fun calcReadable(): Double {
        val multiplication = 10.0.pow(token.decimals)
        return origin.toBigDecimal().toDouble()/multiplication
    }
}