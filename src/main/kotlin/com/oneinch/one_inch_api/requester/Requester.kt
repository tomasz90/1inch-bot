package com.oneinch.one_inch_api.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.on_chain_api.sender.ISender
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.one_inch_api.api.data.SwapDto
import org.springframework.stereotype.Component

@Component
class Requester(val sender: ISender<Transaction>) : AbstractRequester() {

    override fun swap(from: TokenQuote, to: Token) {
        val settings = settings.swapSettings.random()
        val dto = oneInchClient.swap(chain.id, from, to, settings.slippage)
        if (dto != null) {
            val realAdvantage = utils.calculateAdvantage(dto.from, dto.to)
            val isGood = isRateGood(dto.from, dto.to, realAdvantage, settings.advantage)
            if (isGood) {
                val tx = createTx(dto, settings.slippage)
                sender.sendTransaction(tx, from, dto.to)
            }
        }
    }

    private fun createTx(dto: SwapDto, maxSlippage: Double): Transaction {
        val tx = dto.tx
        return Transaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data, maxSlippage)
    }
}

