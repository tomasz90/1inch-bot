package com.oneinch.one_inch_api.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.on_chain_api.sender.Sender
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.one_inch_api.api.data.SwapDto
import com.oneinch.util.SlippageModifier
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.util.*

@Component
class Requester(val sender: Sender, val slippageModifier: SlippageModifier) : AbstractRequester() {

    override suspend fun swap(from: TokenQuote, to: Token) {
        val requestTimestamp = Date()
        val dto = oneInchClient.swap(from, to, settings.allowPartialFill, protocols)
        if (dto != null) {
            val realAdvantage = utils.calculateAdvantage(dto.from, dto.to)
            val isGood = isRateGood(dto.from, dto.to, realAdvantage, settings.advantage)
            if (isGood && !isSwapping.get()) {
                isSwapping.set(true)
                val minReturnAmount = from.calcMinReturnAmountOfDifferentToken(to)
                val newData = slippageModifier.modify(dto.tx.data, minReturnAmount)
                dto.tx.data = newData
                val tx = createTx(dto, minReturnAmount, realAdvantage, requestTimestamp)
                sender.sendTransaction(tx, from, dto.to)
                isSwapping.set(false)
            }
        }
    }

    private fun createTx(dto: SwapDto, minReturnAmount: BigInteger, advantage: Double, requestTimestamp: Date): Transaction {
        val tx = dto.tx
        return Transaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data, minReturnAmount, advantage, requestTimestamp)
    }
}

