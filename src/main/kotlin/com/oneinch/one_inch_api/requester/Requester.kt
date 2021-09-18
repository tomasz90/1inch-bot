package com.oneinch.one_inch_api.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.on_chain_api.sender.Sender
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.one_inch_api.api.data.SwapDto
import org.springframework.stereotype.Component
import java.util.*

@Component
class Requester(val sender: Sender) : AbstractRequester() {

    override suspend fun swap(from: TokenQuote, to: Token) {
        val requestTimestamp = Date()
        val swapSettings = settings.swapSettings.random()
        val dto = oneInchClient.swap(from, to, swapSettings.slippage, settings.allowPartialFill, protocols)
        if (dto != null) {
            val isGood = isRateGood(dto.from, dto.to, swapSettings.advantage)
            if (isGood && !isSwapping.get()) {
                isSwapping.set(true)
                val tx = createTx(dto, swapSettings.slippage, swapSettings.advantage, requestTimestamp)
                sender.sendTransaction(tx, from, dto.to)
                isSwapping.set(false)
            }
        }
    }

    private fun createTx(dto: SwapDto, maxSlippage: Double, advantage: Double, requestTimestamp: Date): Transaction {
        val tx = dto.tx
        return Transaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data, maxSlippage, advantage, requestTimestamp)
    }
}

