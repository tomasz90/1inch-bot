package com.oneinch.api.blockchain.sender

import com.oneinch.`object`.TokenQuote

interface ISender<T> {
    suspend fun sendTransaction(tx: T, from: TokenQuote, to: TokenQuote)
}