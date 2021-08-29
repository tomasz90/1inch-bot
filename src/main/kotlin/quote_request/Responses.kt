package quote_request

import com.fasterxml.jackson.annotation.JsonProperty

class QuoteResponse(
    @JsonProperty("toTokenAmount") val amountReceived: String,
    @JsonProperty("fromToken") val fromToken: TokenProperties,
    @JsonProperty("toToken") val toToken: TokenProperties
)

class ApprovalResponse(
    @JsonProperty("data") val data: String,
    @JsonProperty("value") val value: String,
    @JsonProperty("gasPrice") val gasPrice: String,
    @JsonProperty("to") val to: String
)

class SwapResponse(
    @JsonProperty("tx") val tx: Tx
)

class Tx(
    @JsonProperty("from") val from: String,
    @JsonProperty("to") val to: String,
    @JsonProperty("data") val data: String,
    @JsonProperty("value") val value: String,
    @JsonProperty("gas") val gas: String,
    @JsonProperty("gasPrice") val gasPrice: String
)

class TokenProperties(@JsonProperty("decimals") val decimals: String)