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

    open fun swap(chainId: Int, from: TokenQuote, to: Token){}

    fun isRateGood(from: TokenQuote, to: TokenQuote, percentage: Double): Boolean {
        logRatesInfo(from, to, percentage)
        if (percentage > DEMAND_PERCENT_ADVANTAGE) {
            logSwapInfo(from, to)
            return true
        }
        return false
    }
}

@Component
class Requester(private val sender: ISender<Transaction>) : AbstractRequester() {

    override fun swap(chainId: Int, from: TokenQuote, to: Token) {
        val dto = oneInchClient.swap(chainId, from, to) // TODO: 01.09.2021 change to swap when working
        val tx = createTx(dto)
        val isGood = isRateGood(dto.from, dto.to, dto.percentage)
        if (isGood) sender.sendTransaction(tx)
    }

    private fun createTx(dto: SwapDto): Transaction { // TODO: 01.09.2021 change to swap when working
        val tx = dto.tx
        return Transaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data)
    }
}

@Component
class FakeRequester(private val sender: ISender<FakeTransaction>) : AbstractRequester() {

    override fun swap(chainId: Int, from: TokenQuote, to: Token) {
        val dto = oneInchClient.quote(chainId, from, to) // TODO: 01.09.2021 change to swap when working
        val tx = createTx(dto)
        val isGood = isRateGood(dto.from, dto.to, dto.percentage)
        if (isGood) sender.sendTransaction(tx)
    }

    private fun createTx(dto: QuoteDto): FakeTransaction {
        return FakeTransaction()
    }
}
