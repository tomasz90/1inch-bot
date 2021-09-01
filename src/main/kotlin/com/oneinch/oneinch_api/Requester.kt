package com.oneinch.oneinch_api

import com.oneinch.InputConfig.DEMAND_PERCENT_ADVANTAGE
import com.oneinch.on_chain_api.*
import com.oneinch.oneinch_api.api.data.*
import logRatesInfo
import logSwapInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

abstract class AbstractRequester {

    @Autowired
    lateinit var oneInchClient: OneInchClient

    @Autowired
    lateinit var sender: ISender<ITransaction>

    open fun swap(chainId: Int, from: TokenQuote, to: Token){}

    fun performTxIfGoodRate(from: TokenQuote, to: TokenQuote, percentage: Double, tx: ITransaction) {
        logRatesInfo(from, to, percentage)
        if (percentage > DEMAND_PERCENT_ADVANTAGE) {
            logSwapInfo(from, to)
            sender.sendTransaction(tx)
        }
    }
}

@Component
class Requester : AbstractRequester() {

    override fun swap(chainId: Int, from: TokenQuote, to: Token) {
        val dto = oneInchClient.swap(chainId, from, to) // TODO: 01.09.2021 change to swap when working
        val tx = createTx(dto)
        performTxIfGoodRate(dto.from, dto.to, dto.percentage, tx)
    }

    private fun createTx(dto: SwapDto): ITransaction { // TODO: 01.09.2021 change to swap when working
        val tx = dto.tx
        return Transaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data)
    }
}

@Component
class FakeRequester : AbstractRequester() {

    override fun swap(chainId: Int, from: TokenQuote, to: Token) {
        val dto = oneInchClient.quote(chainId, from, to) // TODO: 01.09.2021 change to swap when working
        val tx = createTx(dto)
        performTxIfGoodRate(dto.from, dto.to, dto.percentage, tx)
    }

    private fun createTx(dto: QuoteDto): ITransaction {
        return FakeTransaction()
    }
}
