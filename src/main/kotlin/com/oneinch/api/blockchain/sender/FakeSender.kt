package com.oneinch.api.blockchain.sender

import com.oneinch.`object`.TokenQuote
import com.oneinch.api.blockchain.tx.FakeTransaction
import com.oneinch.repository.FakeRepositoryManager
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class FakeSender(val repository: FakeRepositoryManager) : AbstractSender<FakeTransaction>() {
    override suspend fun sendTransaction(tx: FakeTransaction, from: TokenQuote, to: TokenQuote) {
        //repository.saveTransaction(from, to, t)
        repository.updateBalances(from, to)

        TimeUnit.SECONDS.sleep(10)
    }
}