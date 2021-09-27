package com.oneinch.requester

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.api.blockchain.balance.Balance
import com.oneinch.api.blockchain.sender.Sender
import com.oneinch.api.blockchain.tx.BasicTransaction
import com.oneinch.api.blockchain.tx.Transaction
import com.oneinch.api.blockchain.tx.TransactionCreator
import com.oneinch.api.one_inch.api.data.SwapDto
import com.oneinch.loader.Settings
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.util.*

@Component
class Requester(
    val sender: Sender,
    val transactionCreator: TransactionCreator,
    val settings: Settings,
    val chain: Chain,
    val balance: Balance
) :
    AbstractRequester() {

    override suspend fun swap(from: TokenQuote, to: Token) {
        val requestTimestamp = Date()
        val slippage = settings.defaultSlippage
        val dto = oneInchClient.swap(from, to, settings.allowPartialFill, protocols, slippage)
        if (dto != null) {
            val realAdvantage = calculateAdvantage(dto)
            if (shouldSwap(realAdvantage)) {
                val tx = createTransaction(dto, realAdvantage, requestTimestamp)
                sender.sendTransaction(tx, from, dto.to)
                val coinQuote = balance.getCoin()
                if (coinQuote != null) {
                    val minimalCoinBalance = settings.minimalCoinBalance
                    if (coinQuote.doubleValue < minimalCoinBalance) {
                        val tokenQuote = balance.getAnyNonZeroERC20()
                        val swapQuote = tokenQuote.calcOrigin(minimalCoinBalance)
                        val swapTokenQuote = TokenQuote(tokenQuote.token, swapQuote)
                        val coinDto = oneInchClient.swap(swapTokenQuote, chain.coin, false, protocols, 5.0)
                        if (coinDto != null) {
                            val basicTransaction = createBasicTransaction(coinDto)
                            sender.sendBasicTransaction(basicTransaction, swapTokenQuote, coinDto.to)
                        }
                    }
                }
                isSwapping.set(false)
            }
        }
    }

    private fun shouldSwap(realAdvantage: Double): Boolean {
        if (realAdvantage < advantageProvider.advantage) {
            return false
        } else if (isSwapping.get()) {
            return false
        }
        isSwapping.set(true)
        return true
    }

    private fun createTransaction(dto: SwapDto, advantage: Double, requestTimestamp: Date): Transaction {
        val minReturnAmount = dto.from.calcMinReturnAmountOfDifferentToken(dto.to.token)
        val tx = dto.tx
        return transactionCreator.create(
            tx.gas,
            tx.value,
            tx.to,
            tx.data,
            minReturnAmount,
            advantage,
            requestTimestamp
        )
    }

    private fun createBasicTransaction(dto: SwapDto): BasicTransaction {
        val tx = dto.tx
        return BasicTransaction(
            tx.gasPrice,
            tx.gas,
            tx.value,
            tx.to,
            tx.data)
    }

    private fun TokenQuote.calcOrigin(quote: Int): BigInteger {
        val decimals = this.token.decimals
        return (quote.toBigDecimal() * decimals).toBigInteger()
    }
}

