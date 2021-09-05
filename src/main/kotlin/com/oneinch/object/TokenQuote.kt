package com.oneinch.`object`

import Const.tokens
import java.math.BigInteger
import kotlin.math.pow

class TokenQuote private constructor(val symbol: String, val address: String) {

    constructor(symbol: String, address: String, origin: BigInteger) : this(symbol, address) {
        this.origin = origin
        this.readable = calcReadable()
    }

    constructor(symbol: String, address: String, readable: Double) : this(symbol, address) {
        this.readable = readable
        this.origin = calcOrigin()
    }

    var readable: Double = 0.0
    lateinit var origin: BigInteger
    private val decimals = tokens.first { it.address == this.address }.decimals
    private val multiplication = calcMultiply()

    private fun calcMultiply() = 10.0.pow(decimals)

    private fun calcOrigin(): BigInteger {
        return (readable*multiplication).toBigDecimal().toBigInteger()
    }

    private fun calcReadable(): Double {
        return origin.toBigDecimal().toDouble()/multiplication
    }
}