package com.oneinch.api.gas_station

import com.fasterxml.jackson.annotation.JsonProperty

class GasResponse(
    @JsonProperty("safeLow") val safeLow: Double,
    @JsonProperty("standard") val standard: Double,
    @JsonProperty("fast") val fast: Double,
    @JsonProperty("fastest") val fastest: Double
)