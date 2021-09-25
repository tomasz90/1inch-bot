package com.oneinch.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.api.blockchain.sender.FakeSender
import com.oneinch.api.blockchain.tx.FakeTransaction
import com.oneinch.api.one_inch.api.data.QuoteDto
import org.springframework.stereotype.Component

@Component
class FakeRequester(val sender: FakeSender) : AbstractRequester() {

    override suspend fun swap(from: TokenQuote, to: Token) {
        val dto = oneInchClient.quote(from, to)
        if(dto != null) {
            val tx = createTx(dto)
            val realAdvantage = calculateAdvantage(dto)
            if (realAdvantage > advantageProvider.advantage) sender.sendTransaction(tx, from, dto.to)
        }
    }

    private fun createTx(dto: QuoteDto): FakeTransaction {
        return FakeTransaction()
    }
}