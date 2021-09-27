package com.oneinch.api.blockchain.sender

import com.oneinch.`object`.TokenQuote

abstract class AbstractSender<T> {

    open suspend fun sendTransaction(tx: T, from: TokenQuote, to: TokenQuote){}
}