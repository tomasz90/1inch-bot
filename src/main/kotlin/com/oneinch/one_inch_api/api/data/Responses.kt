package com.oneinch.one_inch_api.api.data

import com.fasterxml.jackson.annotation.JsonProperty
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import java.math.BigInteger

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
    @JsonProperty("data") val data: String,
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
    val from = TokenQuote(fromToken.symbol, fromToken.address, fromTokenAmount)
    val to = TokenQuote(toToken.symbol, toToken.address, toTokenAmount)
    return QuoteDto(from, to)
}

fun SwapResponse.toDto(): SwapDto {
    val from = TokenQuote(fromToken.symbol, fromToken.address, fromTokenAmount)
    val to = TokenQuote(toToken.symbol, toToken.address, toTokenAmount)
    val tx = this.tx
    return SwapDto(from, to, tx)
}