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
import kotlin.random.Random
import kotlin.random.nextULong

@Component
class Requester(val sender: ISender<Transaction>) : AbstractRequester() {

    override suspend fun swap(from: TokenQuote, to: Token, coroutine: CoroutineScope) {
        val swapId = Random.nextULong().toString()
        getLogger().info("SWAP_ID: $swapId")
        val settings = settings.swapSettings.random()
        val dto = oneInchClient.swap(chain.id, from, to, settings.slippage, true)
        if (dto != null) {
            val realAdvantage = utils.calculateAdvantage(dto.from, dto.to)
            val isGood = isRateGood(dto.from, dto.to, realAdvantage, settings.advantage)
            if (isGood && canEnter) {
                canEnter = false
                getLogger().info("entered to swap")
                coroutineScope {
                    coroutine.cancel()
                    val tx = createTx(dto, settings.slippage)
                    sender.sendTransaction(tx, from, dto.to, swapId)
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

