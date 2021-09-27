package com.oneinch.api.blockchain.sender

import com.oneinch.`object`.TokenQuote
import com.oneinch.api.blockchain.balance.Balance
import com.oneinch.api.blockchain.tx.BasicTransaction
import com.oneinch.api.blockchain.tx.Transaction
import com.oneinch.api.telegram.TelegramClient
import com.oneinch.loader.Settings
import com.oneinch.provider.advantage.AdvantageProvider
import com.oneinch.repository.RealRepositoryManager
import com.oneinch.repository.dao.Status
import com.oneinch.repository.dao.Status.FAIL
import com.oneinch.repository.dao.Status.PARTIALLY
import com.oneinch.repository.dao.Status.PASSED
import com.oneinch.repository.round
import com.oneinch.util.getDuration
import com.oneinch.util.getLogger
import com.oneinch.util.logSwapInfo
import kotlinx.coroutines.delay
import org.springframework.stereotype.Component
import org.web3j.exceptions.MessageDecodingException
import org.web3j.protocol.Web3j
import org.web3j.tx.RawTransactionManager
import java.math.BigInteger
import java.util.*

@Component
class Sender(
    val settings: Settings,
    val manager: RawTransactionManager,
    val repository: RealRepositoryManager,
    val balance: Balance,
    val web3j: Web3j,
    val telegramClient: TelegramClient,
    val advantageProvider: AdvantageProvider
) : AbstractSender<Transaction>() {

    private val ZERO = BigInteger.valueOf(0)
    private val TEN_SECONDS = 10000L

    override suspend fun sendTransaction(tx: Transaction, from: TokenQuote, to: TokenQuote) {
        try {
            val requestTime = getDuration(tx.requestTimestamp)
            val sendTxTimeStamp = Date()
            var toBalance = getBalance(to)
            val balanceBefore = balance.getUsdValue()
            val txHash = send(tx, from, to)
            val txTime = getDuration(sendTxTimeStamp)
            delay(TEN_SECONDS) // to be sure getting valid balance
            balance.updateAll()
            val balanceAfter = balance.getUsdValue()
            val status: Status
            when (getBalance(from)) {
                from.origin -> status = FAIL
                ZERO -> { status = PASSED; advantageProvider.resetToDefault() }
                else -> { status = PARTIALLY; advantageProvider.resetToDefault() }
            }
            toBalance = getBalance(to) - toBalance
            repository.saveTransaction(txHash, tx, requestTime, txTime, sendTxTimeStamp, from, to, toBalance, status)
            val profit = (balanceAfter - balanceBefore).round()
            val coinValue = balance.getCoin()?.calcDoubleValue()?.round()
            if (profit > 10) {
                telegramClient.sendSwapMessage(profit, coinValue)
            }
        } catch (e: MessageDecodingException) {
            getLogger().error("Transaction failed: ${e.stackTrace}")
        }
    }

    suspend fun sendBasicTransaction(tx: BasicTransaction, from: TokenQuote, to: TokenQuote) {
        send(tx, from, to)
        delay(TEN_SECONDS) // to be sure getting valid balance
        val coinBalance = balance.getCoin()?.doubleValue
        if (coinBalance != null) {
            telegramClient.sendRefillGasBalanceMessage(coinBalance)
        }
    }

    private suspend fun send(tx: BasicTransaction, from: TokenQuote, to: TokenQuote): String {
        val txHash = manager.sendTransaction(tx.gasPrice, tx.gasLimit, tx.address, tx.data, tx.value).transactionHash
        logSwapInfo(txHash, from, to)
        waitUntilTxDone(txHash)
        return txHash
    }

    private suspend fun waitUntilTxDone(txHash: String) {
        val result = { -> web3j.ethGetTransactionReceipt(txHash).send().result }
        while (result.invoke() == null) {
            delay(500)
        }
    }

    private fun getBalance(from: TokenQuote): BigInteger {
        return balance.getERC20(from.token)!!.origin
    }
}