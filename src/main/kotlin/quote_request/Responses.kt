package quote_request

import Token
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigInteger

interface SwapQuoteResponse {
    var from: Token
    var to: Token
    var tx: Tx?
}

class QuoteResponse(
    @JsonProperty("fromTokenAmount") val fromTokenAmount: BigInteger,
    @JsonProperty("toTokenAmount") val toTokenAmount: BigInteger,
    @JsonProperty("fromToken") val fromToken: TokenProperties,
    @JsonProperty("toToken") val toToken: TokenProperties
) : SwapQuoteResponse {
    override var from = Token(fromToken.symbol, fromToken.address, fromToken.decimals, fromTokenAmount)
    override var to = Token(toToken.symbol, toToken.address, toToken.decimals, toTokenAmount)
    override var tx: Tx? = null
}

class SwapResponse(
    @JsonProperty("fromTokenAmount") val fromTokenAmount: BigInteger,
    @JsonProperty("toTokenAmount") val toTokenAmount: BigInteger,
    @JsonProperty("fromToken") val fromToken: TokenProperties,
    @JsonProperty("toToken") val toToken: TokenProperties,
    @JsonProperty("tx") override var tx: Tx?
) : SwapQuoteResponse {
    init {
        if (tx == null) throw KotlinNullPointerException("Response doesn't contain tx.")
    }
    override var from = Token(fromToken.symbol, fromToken.address, fromToken.decimals, fromTokenAmount)
    override var to = Token(toToken.symbol, toToken.address, toToken.decimals, toTokenAmount)
}

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