package com.oneinch.repository

import com.oneinch.`object`.TokenQuote
import com.oneinch.on_chain_api.tx.Transaction

class RealRepositoryManager: IRepositoryManager {

    fun saveTransaction(from: TokenQuote, to: TokenQuote, t: Transaction, txHash: String) {
        TODO("Not yet implemented")
    }
}