package com.oneinch.one_inch_api.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.on_chain_api.sender.Sender
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.one_inch_api.api.data.SwapDto
import com.oneinch.util.SlippageModifier
import com.oneinch.util.calculateAdvantage
import org.springframework.stereotype.Component
import java.util.*

@Component
class Requester(val sender: Sender, val slippageModifier: SlippageModifier) : AbstractRequester() {

    override suspend fun swap(from: TokenQuote, to: Token) {
        val requestTimestamp = Date()
        val dto = oneInchClient.swap(from, to, settings.allowPartialFill, protocols)
        if (dto != null) {
            val realAdvantage = calculateAdvantage(dto)
            if (canPerformSwap(dto, realAdvantage)) {
                val tx = createTx(dto, realAdvantage, requestTimestamp)
                sender.sendTransaction(tx, from, dto.to)
                isSwapping.set(false)
            }
        }
    }

    private fun canPerformSwap(dto: SwapDto, realAdvantage: Double): Boolean {
        utils.logRatesInfo(dto, realAdvantage)
        val condition = realAdvantage > settings.advantage && !isSwapping.get()
        if (condition) {
            isSwapping.set(true)
        }
        return condition
    }

    private fun createTx(dto: SwapDto, advantage: Double, requestTimestamp: Date): Transaction {
        val minReturnAmount = dto.from.calcMinReturnAmountOfDifferentToken(dto.to.token)
        val tx = dto.tx
        return Transaction.create(
            settings,
            slippageModifier,
            tx.gasPrice,
            tx.gas,
            tx.value,
            tx.to,
            tx.data,
            minReturnAmount,
            advantage,
            requestTimestamp
        )
    }
}

