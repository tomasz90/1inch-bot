package com.oneinch.`object`

import java.math.BigInteger
import kotlin.math.pow

class TokenQuote(val token: Token, val origin: BigInteger) {

    fun calcReadable(): Double {
        val multiplication = token.decimals.toBigDecimal()
        return origin.toBigDecimal().divide(multiplication).toDouble()
    }
}