package com.oneinch.on_chain_api.sender

import com.oneinch.`object`.TokenQuote

interface ISender<T> {
    fun sendTransaction(t: T, from: TokenQuote, to: TokenQuote)
}