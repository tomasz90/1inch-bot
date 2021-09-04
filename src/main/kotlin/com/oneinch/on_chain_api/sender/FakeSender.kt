package com.oneinch.on_chain_api.sender

import com.oneinch.repository.FakeRepositoryManager
import com.oneinch.on_chain_api.tx.FakeTransaction
import com.oneinch.`object`.TokenQuote
import org.springframework.stereotype.Component

@Component
class FakeSender(val repository: FakeRepositoryManager) : ISender<FakeTransaction> {
    override fun sendTransaction(t: FakeTransaction, from: TokenQuote, to: TokenQuote) {
        //repository.saveTransaction(from, to, t)
        repository.updateBalance(from, to)
    }
}