package com.oneinch.on_chain_api.sender

import com.oneinch.`object`.TokenQuote

interface ISender<T> {
    suspend fun sendTransaction(tx: T, from: TokenQuote, to: TokenQuote)
}