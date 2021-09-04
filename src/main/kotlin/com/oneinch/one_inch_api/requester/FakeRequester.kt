package com.oneinch.one_inch_api.requester

import com.oneinch.on_chain_api.sender.ISender
import com.oneinch.on_chain_api.tx.FakeTransaction
import com.oneinch.one_inch_api.api.data.QuoteDto
import com.oneinch.one_inch_api.api.data.Token
import com.oneinch.one_inch_api.api.data.TokenQuote
import org.springframework.stereotype.Component

@Component
class FakeRequester(private val sender: ISender<FakeTransaction>) : AbstractRequester() {

    override fun swap(chainId: Int, from: TokenQuote, to: Token) {
        val dto = oneInchClient.quote(chainId, from, to) // TODO: 01.09.2021 change to swap when working
        val tx = createTx(dto)
        val isGood = isRateGood(dto.from, dto.to, dto.percentage)
        if (isGood) sender.sendTransaction(tx, from)
    }

    private fun createTx(dto: QuoteDto): FakeTransaction {
        return FakeTransaction()
    }
}