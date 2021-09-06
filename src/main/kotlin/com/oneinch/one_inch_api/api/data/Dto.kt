package com.oneinch.one_inch_api.api.data

import com.oneinch.`object`.TokenQuote

class QuoteDto(val from: TokenQuote, val to: TokenQuote)

class SwapDto(val from: TokenQuote, val to: TokenQuote, val tx: Tx)

