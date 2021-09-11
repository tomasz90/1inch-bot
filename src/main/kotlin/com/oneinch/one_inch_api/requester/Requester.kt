package com.oneinch.one_inch_api.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.getLogger
import com.oneinch.on_chain_api.sender.ISender
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.one_inch_api.api.data.SwapDto
import com.oneinch.one_inch_api.requester.EnterSwap.canEnter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Component

@Component
class Requester(val sender: ISender<Transaction>) : AbstractRequester() {

    override suspend fun swap(from: TokenQuote, to: Token, coroutine: CoroutineScope) {
        val slippageAdvantage = settings.advantage.random()
        val dto = oneInchClient.swap(chain.id, from, to, slippageAdvantage)
        if (dto != null) {
            val advantage = utils.calculateAdvantage(dto.from, dto.to)
            val isGood = isRateGood(dto.from, dto.to, advantage, slippageAdvantage)
            if (isGood && canEnter) {
                canEnter = false
                getLogger().info("entered to swap")
                coroutineScope {
                    coroutine.cancel()
                    val tx = createTx(dto, slippageAdvantage)
                    sender.sendTransaction(tx, from, dto.to)
                }
            }
        }
    }

    private fun createTx(dto: SwapDto, maxSlippage: Double): Transaction {
        val tx = dto.tx
        return Transaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data, maxSlippage)
    }
}

object EnterSwap {
    var canEnter = true
}

