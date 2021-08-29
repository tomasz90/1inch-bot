package quote_request

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigInteger

class QuoteResponse(
    @JsonProperty("fromTokenAmount") val fromTokenAmount: BigInteger,
    @JsonProperty("toTokenAmount") val toTokenAmount: BigInteger,
    @JsonProperty("fromToken") val fromToken: TokenProperties,
    @JsonProperty("toToken") val toToken: TokenProperties
)

class ApprovalResponse(
    @JsonProperty("data") val data: String,
    @JsonProperty("value") val value: BigInteger,
    @JsonProperty("gasPrice") val gasPrice: BigInteger,
    @JsonProperty("to") val to: String
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