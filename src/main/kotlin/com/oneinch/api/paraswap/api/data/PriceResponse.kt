package com.oneinch.api.paraswap.api.data

import com.oneinch.api.paraswap.api.Side

class PriceResponse(val priceRoute: PriceRoute)

class PriceRoute(
    val blockNumber: Int,
    val network: Int,
    val srcToken: String,
    val srcDecimals: Int,
    val srcAmount: String,
    val destToken: String,
    val destDecimals: String,
    val destAmount: String,
    val bestRoute: Route,
    val others: Others,
    val gasCost: String,
    val side: Side,
    val tokenTransferProxy: String,
    val contractAddress: String,
    val contractMethod: String,
    val srcUSD: String,
    val destUSD: String,
    val partner: String,
    val partnerFee: Int,
    val maxImpactReached: Boolean,
    val hmac: String
)

class Route(
    val percent: Int,
    val swaps: List<Swaps>
)

class Swaps(
    val srcToken: String,
    val srcDecimals: Int,
    val destToken: String,
    val swapExchanges: SwapExchanges
)

class SwapExchanges(
    val exchange: String,
    val srcAmount: String,
    val destAmount: String,
    val percent: Int,
    val data: Any
)

class Others(
    val exchange: String,
    val srcAmount: String,
    val destAmount: String,
    val unit: String,
    val data: Any
)

