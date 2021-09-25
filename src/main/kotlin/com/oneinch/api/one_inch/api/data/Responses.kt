package com.oneinch.api.one_inch.api.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.pow

class QuoteResponse(
    @JsonProperty("fromTokenAmount") val fromTokenAmount: BigInteger,
    @JsonProperty("toTokenAmount") val toTokenAmount: BigInteger,
    @JsonProperty("fromToken") val fromToken: TokenProperties,
    @JsonProperty("toToken") val toToken: TokenProperties
)

class SwapResponse(
    @JsonProperty("fromTokenAmount") val fromTokenAmount: BigInteger,
    @JsonProperty("toTokenAmount") val toTokenAmount: BigInteger,
    @JsonProperty("fromToken") val fromToken: TokenProperties,
    @JsonProperty("toToken") val toToken: TokenProperties,
    @JsonProperty("tx") val tx: Tx
)

class Tx(
    @JsonProperty("from") val from: String,
    @JsonProperty("to") val to: String,
    @JsonProperty("data") var data: String,
    @JsonProperty("value") val value: BigInteger,
    @JsonProperty("gas") val gas: BigInteger,
    @JsonProperty("gasPrice") val gasPrice: BigInteger
)

class TokenProperties(
    @JsonProperty("symbol") val symbol: String,
    @JsonProperty("decimals") val decimals: Int,
    @JsonProperty("address") val address: String
)

fun QuoteResponse.toDto(): QuoteDto {
    val fromToken = Token(fromToken.symbol, fromToken.address, fromToken.decimals.toFullDecimals())
    val from = TokenQuote(fromToken, fromTokenAmount)

    val toToken = Token(toToken.symbol, toToken.address, toToken.decimals.toFullDecimals())
    val to = TokenQuote(toToken, toTokenAmount)
    return QuoteDto(from, to)
}

fun SwapResponse.toDto(): SwapDto {
    val fromToken = Token(fromToken.symbol, fromToken.address, fromToken.decimals.toFullDecimals())
    val from = TokenQuote(fromToken, fromTokenAmount)
    val toToken = Token(toToken.symbol, toToken.address, toToken.decimals.toFullDecimals())
    val to = TokenQuote(toToken, toTokenAmount)
    val tx = this.tx
    return SwapDto(from, to, tx)
}

private fun Int.toFullDecimals(): BigDecimal {
    return 10.0.pow(this).toBigDecimal()
}