package com.oneinch.one_inch_api.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.on_chain_api.sender.ISender
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.one_inch_api.api.data.SwapDto
import org.springframework.stereotype.Component

@Component
class Requester(val sender: ISender<Transaction>) : AbstractRequester() {

    override fun swap(chainId: Int, from: TokenQuote, to: Token) {
        val dto = oneInchClient.swap(chainId, from, to)
        if(dto != null) {
            val tx = createTx(dto)
            val isGood = isRateGood(dto.from, dto.to, utils.calculateAdvantage(dto.from, dto.to))
            if (isGood) sender.sendTransaction(tx, from, dto.to)
        }
    }

    private fun createTx(dto: SwapDto): Transaction {
        val tx = dto.tx
        return Transaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data)
    }
}

