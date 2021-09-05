package com.oneinch.one_inch_api.requester

import com.oneinch.on_chain_api.sender.ISender
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.one_inch_api.api.data.SwapDto
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import org.springframework.stereotype.Component

@Component
class Requester(private val sender: ISender<Transaction>) : AbstractRequester() {

    override fun swap(chainId: Int, from: TokenQuote, to: Token) {
        val dto = oneInchClient.swap(chainId, from, to) // TODO: 01.09.2021 change to swap when working
        if(dto != null) {
            val tx = createTx(dto)
            val isGood = isRateGood(dto.from, dto.to, dto.percentage)
            if (isGood) sender.sendTransaction(tx, from, dto.to)
        }
    }

    private fun createTx(dto: SwapDto): Transaction {
        val tx = dto.tx
        return Transaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data)
    }
}

