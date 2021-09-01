package com.oneinch.oneinch_api

import com.oneinch.InputConfig
import com.oneinch.InputConfig.DEMAND_PERCENT_ADVANTAGE
import com.oneinch.on_chain_api.*
import com.oneinch.oneinch_api.api.data.QuoteDto
import com.oneinch.oneinch_api.api.data.SwapDto
import com.oneinch.oneinch_api.api.data.Token
import com.oneinch.oneinch_api.api.data.TokenQuote
import logRatesInfo
import logSwapInfo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

abstract class AbstractRequester {

    @Autowired
    lateinit var sender: ISender<ITransaction>

    open fun swap(chainId: Int, from: TokenQuote, to: Token) {}

    fun performTxIfGoodRate(t1: TokenQuote, t2: TokenQuote, percentage: Double, tx: ITransaction) {
        if (percentage > DEMAND_PERCENT_ADVANTAGE) {
            logSwapInfo(t1, t2)
            sender.sendTransaction(tx)
        }
    }
}

@Component
class Requester(private val oneInchClient: OneInchClient) : AbstractRequester() {
    override fun swap(chainId: Int, from: TokenQuote, to: Token) {
        val dto = oneInchClient.swap(chainId, from, to) // TODO: 01.09.2021 change to swap when working
        logRatesInfo(dto.from, dto.to, dto.percentage)
        val tx = createTx(dto)
        performTxIfGoodRate(dto.from, dto.to, dto.percentage, tx)
    }

    fun createTx(dto: SwapDto): ITransaction { // TODO: 01.09.2021 change to swap when working
        val tx = dto.tx
        return Transaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data)
    }
}

@Component
class FakeRequester(private val oneInchClient: OneInchClient) : AbstractRequester() {
    override fun swap(chainId: Int, from: TokenQuote, to: Token) {
        val dto = oneInchClient.quote(chainId, from, to)
        logRatesInfo(dto.from, dto.to, dto.percentage)
        val tx = createTx(dto)
        performTxIfGoodRate(dto.from, dto.to, dto.percentage, tx)
    }

    fun createTx(dto: QuoteDto): ITransaction {
        return FakeTransaction()
    }
}
