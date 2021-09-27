package com.oneinch.`object`

import java.math.BigDecimal

open class Coin(val symbol: String, val address: String, val decimals: BigDecimal)

class Token(symbol: String, address: String, decimals: BigDecimal): Coin(symbol, address, decimals)
