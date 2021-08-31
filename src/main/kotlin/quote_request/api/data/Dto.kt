package quote_request.api.data

import calculateAdvantage
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.properties.Delegates

class QuoteDto(val from: TokenQuote, val to: TokenQuote) {
    val percentage = calculateAdvantage(from, to)
}

class SwapDto(val from: TokenQuote, val to: TokenQuote, val tx: Tx) {
    val percentage = calculateAdvantage(from, to)
}

class Token(val symbol: String, val address: String, val decimals: Int)

class TokenQuote private constructor(val token: Token) {

    constructor(token: Token, origin: BigInteger) : this(token) {
        this.origin = origin
        this.multiplication = calcMultiply()
        this.readable = calcReadable()
    }

    constructor(token: Token, readable: Double) : this(token) {
        this.readable = readable
        this.multiplication = calcMultiply()
        this.origin = calcOrigin()

    }

    var readable by Delegates.notNull<Double>()
    lateinit var origin: BigInteger
    private lateinit var multiplication: BigDecimal

    private fun calcMultiply() = BigDecimal.valueOf(10L).pow(token.decimals)

    private fun calcOrigin(): BigInteger {
        val bigDec = readable.toBigDecimal()
        return bigDec.multiply(multiplication).toBigInteger()
    }

    private fun calcReadable(): Double {
        val bigDec = origin.toBigDecimal()
        return bigDec.divide(multiplication).toDouble()
    }
}