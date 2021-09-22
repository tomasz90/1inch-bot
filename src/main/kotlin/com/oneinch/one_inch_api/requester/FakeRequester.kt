package com.oneinch.one_inch_api.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.on_chain_api.sender.FakeSender
import com.oneinch.on_chain_api.tx.FakeTransaction
import com.oneinch.one_inch_api.api.data.QuoteDto
import com.oneinch.util.calculateAdvantage
import org.springframework.stereotype.Component

@Component
class FakeRequester(val sender: FakeSender) : AbstractRequester() {

    override suspend fun swap(from: TokenQuote, to: Token) {
        val dto = oneInchClient.quote(from, to)
        if(dto != null) {
            val tx = createTx(dto)
            val realAdvantage = calculateAdvantage(dto)
            utils.logRatesInfo(dto, realAdvantage)
            if (realAdvantage > settings.advantage) sender.sendTransaction(tx, from, dto.to)
        }
    }

    private fun createTx(dto: QuoteDto): FakeTransaction {
        return FakeTransaction()
    }
}