package com.oneinch.on_chain_api.sender

import com.oneinch.`object`.TokenQuote
import com.oneinch.config.Settings
import com.oneinch.on_chain_api.balance.Balance
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.repository.RealRepositoryManager
import com.oneinch.repository.dao.Status.FAIL
import com.oneinch.repository.dao.Status.PASSED
import com.oneinch.util.getLogger
import com.oneinch.util.logSwapInfo
import kotlinx.coroutines.delay
import org.springframework.stereotype.Component
import org.web3j.exceptions.MessageDecodingException
import org.web3j.protocol.Web3j
import org.web3j.tx.RawTransactionManager

@Component
class Sender(
    val settings: Settings,
    val rawTransactionManager: RawTransactionManager,
    val repository: RealRepositoryManager,
    val balance: Balance,
    val web3j: Web3j
) : ISender<Transaction> {

    override suspend fun sendTransaction(tx: Transaction, from: TokenQuote, to: TokenQuote) {
        try {
            val txHash = send(tx, from, to)
            val status = if (sameBalance(from)) FAIL else PASSED
            repository.saveTransaction(txHash, tx, from, to, status)
            balance.updateAll()
        } catch (e: MessageDecodingException) {
            getLogger().error("Transaction failed: ${e.stackTrace}")
        }
    }

    private suspend fun send(tx: Transaction, from: TokenQuote, to: TokenQuote): String {
        val txHash = rawTransactionManager
            .sendTransaction(tx.gasPrice, tx.gasLimit, tx.address, tx.data, tx.value)
            .transactionHash
        logSwapInfo(txHash, from, to)
        waitUntilTxDone(txHash)
        return txHash
    }

    private suspend fun waitUntilTxDone(txHash: String) {
        val result = { -> web3j.ethGetTransactionReceipt(txHash).send().result }
        while (result.invoke() == null) {
            delay(5000)
        }
    }

    private fun sameBalance(from: TokenQuote): Boolean {
        return balance.getERC20(from.token)?.origin == from.origin
    }
}