package com.one_inch

import MATIC
import com.one_inch.common.Chain

object InputConfig {

    val CHAIN: Chain = MATIC
    const val TEST_MODE = true
    const val MY_ADDRESS = "0x0fad488c45e44B72A17e4eBFc20ce16ff284de3E"
    const val LOG_DECIMAL_PRECISION = 0

    const val AMOUNT_TO_SELL: Double = 20000.0
    const val DEMAND_PERCENT_ADVANTAGE: Double = 1.0
    const val MAX_SLIPPAGE: Double = 0.1
    const val INCREASED_GAS_LIMIT = 1.25

}