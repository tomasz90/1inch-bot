package com.oneinch.one_inch_api.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.on_chain_api.sender.ISender
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.one_inch_api.api.data.SwapDto
import org.springframework.stereotype.Component
import java.util.*

@Component
class Requester(val sender: ISender<Transaction>) : AbstractRequester() {

    override suspend fun swap(from: TokenQuote, to: Token) {
        timer.addCall()
        val requestTimestamp = Date()
        val swapSettings = settings.swapSettings.random()
        val dto = oneInchClient.swap(chain.id, from, to, swapSettings.slippage, settings.allowPartialFill, protocols.asString())
        if (dto != null) {
            val realAdvantage = utils.calculateAdvantage(dto.from, dto.to)
            val isGood = isRateGood(dto.from, dto.to, realAdvantage, swapSettings.advantage)
            if (isGood && !isSwapping.get()) {
                isSwapping.set(true)
                val tx = createTx(dto, swapSettings.slippage, requestTimestamp)
                sender.sendTransaction(tx, from, dto.to)
                isSwapping.set(false)
            }
        }
    }

    private fun createTx(dto: SwapDto, maxSlippage: Double, requestTimestamp: Date): Transaction {
        val tx = dto.tx
        return Transaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data, maxSlippage, requestTimestamp)
    }
}

