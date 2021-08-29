package quote_request

import com.fasterxml.jackson.annotation.JsonProperty

class OneInchResponse(
    @JsonProperty("toTokenAmount") val amountReceived: String,
    @JsonProperty("fromToken") val fromToken: TokenProperties,
    @JsonProperty("toToken") val toToken: TokenProperties
)

class TokenProperties(@JsonProperty("decimals") val decimals: String)