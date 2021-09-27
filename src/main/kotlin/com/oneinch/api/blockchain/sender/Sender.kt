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
    private val `20_SECONDS` = 20000L

    override suspend fun sendTransaction(tx: Transaction, from: TokenQuote, to: TokenQuote) {
        try {
            val toBalance = getBalance(to)
            val allBalanceBefore = balance.getUsdValue()
            val sendTxTimeStamp = Date()
            val txHash = send(tx, from, to)
            val txTime = getDuration(sendTxTimeStamp)
            delay(`20_SECONDS`) // to be sure getting valid balance
            balance.updateAll()
            sendTelegramWhenInProfit(balance.getUsdValue(), allBalanceBefore)
            val status = getTransactionStatus(from)
            repository.saveTransaction(txHash, tx, txTime, sendTxTimeStamp, from, to, getBalance(to) - toBalance, status)
        } catch (e: MessageDecodingException) {
            getLogger().error("Transaction failed: ${e.stackTrace}")
        }
    }

    suspend fun sendBasicTransaction(tx: BasicTransaction, from: TokenQuote, to: TokenQuote) {
        send(tx, from, to)
        delay(`20_SECONDS`) // to be sure getting valid balance
        val coinBalance = balance.getCoin()?.doubleValue
        if (coinBalance != null) {
            telegramClient.sendRefillGasBalanceMessage(coinBalance.round())
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

    private fun getTransactionStatus(from: TokenQuote): Status {
        val status: Status
        when (getBalance(from)) {
            from.origin -> status = FAIL
            ZERO -> { status = PASSED; advantageProvider.resetToDefault() }
            else -> { status = PARTIALLY; advantageProvider.resetToDefault() }
        }
        return status
    }

    private fun sendTelegramWhenInProfit(allBalanceAfter: Double, allBalanceBefore: Double) {
        val profit = (allBalanceAfter - allBalanceBefore).round()
        val coinValue = balance.getCoin()?.doubleValue?.round()
        if (profit > 10) {
            telegramClient.sendSwapMessage(profit, coinValue)
        }
    }

    private fun getBalance(from: TokenQuote): BigInteger {
        return balance.getERC20(from.token)!!.origin
    }
}