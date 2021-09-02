package com.oneinch.config

import com.oneinch.common.Chain
import com.oneinch.common.MATIC

object InputConfig {

    val CHAIN: Chain = MATIC
    const val LOG_DECIMAL_PRECISION = 3

    const val MINIMAL_SWAP_QUOTE: Double = 10.0
    const val AMOUNT_TO_SELL: Double = 20000.0
    const val DEMAND_PERCENT_ADVANTAGE: Double = 1.0
    const val MAX_SLIPPAGE: Double = 0.1
    const val INCREASED_GAS_LIMIT = 1.25

}