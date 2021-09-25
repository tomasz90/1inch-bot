package com.oneinch.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.api.blockchain.sender.Sender
import com.oneinch.api.blockchain.tx.Transaction
import com.oneinch.api.one_inch.api.data.SwapDto
import com.oneinch.util.GasPriceProvider
import com.oneinch.util.SlippageModifier
import org.springframework.stereotype.Component
import java.util.*

@Component
class Requester(val sender: Sender, val slippageModifier: SlippageModifier, val gasPriceProvider: GasPriceProvider) :
    AbstractRequester() {

    override suspend fun swap(from: TokenQuote, to: Token) {
        val requestTimestamp = Date()
        val dto = oneInchClient.swap(from, to, settings.allowPartialFill, protocols)
        if (dto != null) {
            val realAdvantage = calculateAdvantage(dto)
            utils.logRatesInfo(dto, realAdvantage)
            if (shouldSwap(realAdvantage)) {
                val tx = createTx(dto, realAdvantage, requestTimestamp)
                sender.sendTransaction(tx, from, dto.to)
                isSwapping.set(false)
            }
        }
    }

    private fun shouldSwap(realAdvantage: Double): Boolean {
        if (realAdvantage < settings.minAdvantage) {
            return false
        } else if (isSwapping.get()) {
            return false
        }
        isSwapping.set(true)
        return true
    }

    private fun createTx(dto: SwapDto, advantage: Double, requestTimestamp: Date): Transaction {
        val minReturnAmount = dto.from.calcMinReturnAmountOfDifferentToken(dto.to.token)
        val tx = dto.tx
        return Transaction.create(
            settings,
            slippageModifier,
            gasPriceProvider,
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

