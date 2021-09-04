package com.oneinch.on_chain_api.sender

import com.oneinch.on_chain_api.tx.FakeTransaction
import com.oneinch.one_inch_api.api.data.TokenQuote
import org.springframework.stereotype.Component

@Component
class FakeSender : ISender<FakeTransaction> {
    override fun sendTransaction(t: FakeTransaction, from: TokenQuote) {
    }
}