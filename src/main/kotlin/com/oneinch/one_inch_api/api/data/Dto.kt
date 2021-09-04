package com.oneinch.one_inch_api.api.data

import calculateAdvantage
import com.oneinch.`object`.TokenQuote

interface IDto

// TODO: 04.09.2021 check this interface
class QuoteDto(val from: TokenQuote, val to: TokenQuote): IDto {
    val percentage = calculateAdvantage(from, to)
}

class SwapDto(val from: TokenQuote, val to: TokenQuote, val tx: Tx): IDto {
    val percentage = calculateAdvantage(from, to)
}

