package com.oneinch.on_chain_api.sender

import com.oneinch.one_inch_api.api.data.TokenQuote

interface ISender<T> {
    fun sendTransaction(t: T, from: TokenQuote, to: TokenQuote)
}