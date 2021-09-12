package com.oneinch.on_chain_api.sender

import com.oneinch.repository.FakeRepositoryManager
import com.oneinch.on_chain_api.tx.FakeTransaction
import com.oneinch.`object`.TokenQuote
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class FakeSender(val repository: FakeRepositoryManager) : ISender<FakeTransaction> {
    override suspend fun sendTransaction(t: FakeTransaction, from: TokenQuote, to: TokenQuote, string: String) {
        //repository.saveTransaction(from, to, t)
        repository.updateBalances(from, to)
        TimeUnit.SECONDS.sleep(10)
    }
}