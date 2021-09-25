package com.oneinch.api.blockchain.sender

import com.oneinch.`object`.TokenQuote
import com.oneinch.api.blockchain.balance.Balance
import com.oneinch.api.blockchain.tx.Transaction
import com.oneinch.loader.Settings
import com.oneinch.repository.RealRepositoryManager
import com.oneinch.repository.dao.Status
import com.oneinch.repository.dao.Status.FAIL
import com.oneinch.repository.dao.Status.PASSED
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
    val web3j: Web3j
) : AbstractSender<Transaction>() {

    val ZERO = BigInteger.valueOf(0)

    override suspend fun sendTransaction(tx: Transaction, from: TokenQuote, to: TokenQuote) {
        try {
            val requestTimeS = getDuration(tx.requestTimestamp)
            val sendTxTimeStamp = Date()
            var toBalance = getBalance(to)
            val txHash = send(tx, from, to)
            val txTimeS = getDuration(sendTxTimeStamp)
            delay(10000) // to be sure getting valid balance
            // TODO: 24.09.2021 get sum of all balance after update all substract and send telegram message when profit
            balance.updateAll()
            val status: Status
            when (getBalance(from)) {
                from.origin -> status = FAIL
                ZERO -> { status = PASSED; advantageProvider.resetToDefault() }
                else -> { status = PASSED; advantageProvider.resetToDefault() }
            }
            toBalance = getBalance(to) - toBalance
            repository.saveTransaction(txHash, tx, requestTimeS, txTimeS, sendTxTimeStamp, from, to, toBalance, status)
        } catch (e: MessageDecodingException) {
            getLogger().error("Transaction failed: ${e.stackTrace}")
        }
    }

    private suspend fun send(tx: Transaction, from: TokenQuote, to: TokenQuote): String {
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