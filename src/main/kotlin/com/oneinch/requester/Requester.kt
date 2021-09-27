package com.oneinch.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.api.blockchain.balance.Balance
import com.oneinch.api.blockchain.sender.Sender
import com.oneinch.api.blockchain.tx.BasicTransaction
import com.oneinch.api.blockchain.tx.Transaction
import com.oneinch.api.blockchain.tx.TransactionCreator
import com.oneinch.api.one_inch.api.data.SwapDto
import com.oneinch.loader.Settings
import com.oneinch.util.getDuration
import com.oneinch.util.getLogger
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.util.*

@Component
class Requester(
    val sender: Sender,
    val transactionCreator: TransactionCreator,
    val settings: Settings,
    val balance: Balance
) : AbstractRequester() {

    override suspend fun swap(from: TokenQuote, to: Token) {
        val requestTimestamp = Date()
        val slippage = settings.defaultSlippage
        val dto = oneInchClient.swap(from, to, settings.allowPartialFill, protocols, slippage)
        if (dto != null) {
            val requestDuration = getDuration(requestTimestamp)
            val realAdvantage = calculateAdvantage(dto)
            if (shouldSwap(realAdvantage)) {
                val tx = createTransaction(dto, realAdvantage, requestDuration)
                sender.sendTransaction(tx, from, dto.to)
                refillCoinBalanceIfNeeded()
                isSwapping.set(false)
            }
        }
    }

    private suspend fun refillCoinBalanceIfNeeded() {
        val coinQuote = balance.getCoin()
        if (coinQuote != null) {
            val minimalCoinBalance = settings.minimalCoinBalance
            if (coinQuote.doubleValue < minimalCoinBalance) {
                getLogger().info("Refilling gas balance.")
                val tokenQuote = pickTokenToSwap(minimalCoinBalance)
                val coinDto = oneInchClient.swap(tokenQuote, coinQuote.coin, false, protocols, 5.0)
                if (coinDto != null) {
                    val basicTransaction = createBasicTransaction(coinDto)
                    sender.sendBasicTransaction(basicTransaction, tokenQuote, coinDto.to)
                }
            }
        }
    }

    private fun pickTokenToSwap(amount: Int): TokenQuote {
        val tokenQuote = balance.getAnyNonZeroERC20()
        val swapQuote = tokenQuote.calcOrigin(amount)
        return TokenQuote(tokenQuote.token, swapQuote)
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

    private fun createTransaction(dto: SwapDto, advantage: Double, requestDuration: Double): Transaction {
        return transactionCreator.create(dto, advantage, requestDuration)
    }

    private fun createBasicTransaction(dto: SwapDto): BasicTransaction {
        return transactionCreator.createBasic(dto)
    }

    private fun TokenQuote.calcOrigin(quote: Int): BigInteger {
        val decimals = this.token.decimals
        return (quote.toBigDecimal() * decimals).toBigInteger()
    }
}

