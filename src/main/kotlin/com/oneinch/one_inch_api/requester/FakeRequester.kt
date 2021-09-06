package com.oneinch.one_inch_api.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.on_chain_api.sender.ISender
import com.oneinch.on_chain_api.tx.FakeTransaction
import com.oneinch.one_inch_api.api.data.QuoteDto
import org.springframework.stereotype.Component

@Component
class FakeRequester(val sender: ISender<FakeTransaction>) : AbstractRequester() {

    override fun swap(chainId: Int, from: TokenQuote, to: Token) {
        val dto = oneInchClient.quote(chainId, from, to)
        if(dto != null) {
            val tx = createTx(dto)
            val isGood = isRateGood(dto.from, dto.to, utils.calculateAdvantage(dto.from, dto.to))
            if (isGood) sender.sendTransaction(tx, from, dto.to)
        }
    }

    private fun createTx(dto: QuoteDto): FakeTransaction {
        return FakeTransaction()
    }
}