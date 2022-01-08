package com.oneinch.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.api.blockchain.balance.Balance
import com.oneinch.api.blockchain.sender.Sender
import com.oneinch.api.blockchain.tx.BasicTransaction
import com.oneinch.api.blockchain.tx.Transaction
import com.oneinch.api.blockchain.tx.TransactionCreator
import com.oneinch.api.one_inch.OneInchClient
import com.oneinch.api.one_inch.api.data.Dto
import com.oneinch.api.one_inch.api.data.SwapDto
import com.oneinch.loader.Settings
import com.oneinch.util.Utils
import com.oneinch.util.getDuration
import com.oneinch.util.getLogger
import kotlinx.coroutines.sync.Mutex
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.*

@Component
class Requester(
    val sender: Sender,
    val oneInchClient: OneInchClient,
    val transactionCreator: TransactionCreator,
    val protocols: String,
    val settings: Settings,
    val balance: Balance,
    val isSwapping: Mutex,
    val utils: Utils
) {

    suspend fun swap(from: TokenQuote, to: Token) {
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
                isSwapping.unlock()
            }
        }
    }

    private suspend fun refillCoinBalanceIfNeeded() {
        val coinQuote = balance.getCoin()
        if (coinQuote != null) {
            if (coinQuote.doubleValue < settings.minimalCoinBalance) {
                getLogger().info("Refilling gas balance.")
                val tokenQuote = pickTokenToSwap(settings.refillCoinQuote)
                val coinDto = oneInchClient.swap(tokenQuote, coinQuote.coin, false, protocols, 5.0)
                if (coinDto != null) {
                    val basicTransaction = createBasicTransaction(coinDto)
                    sender.sendBasicTransaction(basicTransaction, tokenQuote, coinDto.to)
                }
            }
        }
    }

    private fun pickTokenToSwap(amount: Double): TokenQuote {
        val tokenQuote = balance.getAnyTokenWithBalance(amount)
        val quote = tokenQuote.calcOrigin(amount)
        return TokenQuote(tokenQuote.token, quote)
    }

    private suspend fun shouldSwap(realAdvantage: Double): Boolean {
        if (realAdvantage < settings.minAdvantage) {
            return false
        } else if (isSwapping.isLocked) {
            return false
        }
        isSwapping.lock()
        return true
    }

    private fun createTransaction(dto: SwapDto, advantage: Double, requestDuration: Double): Transaction {
        return transactionCreator.create(dto, advantage, requestDuration)
    }

    private fun createBasicTransaction(dto: SwapDto): BasicTransaction {
        return transactionCreator.createBasic(dto)
    }

    fun calculateAdvantage(dto: Dto): Double {
        val realAdvantage = (dto.to.usdValue - dto.from.usdValue) / dto.from.usdValue * 100
        utils.logRatesInfo(dto, realAdvantage)
        return realAdvantage
    }
}

