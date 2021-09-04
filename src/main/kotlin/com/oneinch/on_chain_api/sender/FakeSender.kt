package com.oneinch.on_chain_api.sender

import com.oneinch.repository.Repository
import com.oneinch.on_chain_api.tx.FakeTransaction
import com.oneinch.one_inch_api.api.data.TokenQuote
import org.springframework.stereotype.Component

@Component
class FakeSender(val repository: Repository) : ISender<FakeTransaction> {
    override fun sendTransaction(t: FakeTransaction, from: TokenQuote, to: TokenQuote) {
        repository.saveTransaction(from, to, t)
        repository.updateBalance(from, to)
    }
}