package com.oneinch.one_inch_api.api.data

import com.oneinch.`object`.TokenQuote

open class Dto(open val from: TokenQuote, open val to: TokenQuote)

class QuoteDto(override val from: TokenQuote, override val to: TokenQuote): Dto(from,to)

class SwapDto(override val from: TokenQuote, override val to: TokenQuote, val tx: Tx): Dto(from,to)

