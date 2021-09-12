package com.oneinch.one_inch_api.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.on_chain_api.sender.ISender
import com.oneinch.on_chain_api.tx.FakeTransaction
import com.oneinch.one_inch_api.api.data.QuoteDto
import kotlinx.coroutines.CoroutineScope
import org.springframework.stereotype.Component

@Component
class FakeRequester(val sender: ISender<FakeTransaction>) : AbstractRequester() {

    override suspend fun swap(from: TokenQuote, to: Token) {
        timer.addCall()
        val demandAdvantage = settings.swapSettings.random().advantage
        val dto = oneInchClient.quote(chain.id, from, to)
        if(dto != null) {
            val tx = createTx(dto)
            val isGood = isRateGood(dto.from, dto.to, utils.calculateAdvantage(dto.from, dto.to), demandAdvantage)
            if (isGood) sender.sendTransaction(tx, from, dto.to)
        }
    }

    private fun createTx(dto: QuoteDto): FakeTransaction {
        return FakeTransaction()
    }
}