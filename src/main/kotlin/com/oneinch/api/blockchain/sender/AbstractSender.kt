package com.oneinch.api.blockchain.sender

import com.oneinch.`object`.TokenQuote
import com.oneinch.provider.AdvantageProvider
import org.springframework.beans.factory.annotation.Autowired

abstract class AbstractSender<T> {

    @Autowired
    lateinit var advantageProvider: AdvantageProvider

    open suspend fun sendTransaction(tx: T, from: TokenQuote, to: TokenQuote){}
}