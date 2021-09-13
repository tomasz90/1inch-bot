package com.oneinch.on_chain_api.sender

import com.oneinch.`object`.TokenQuote
import com.oneinch.config.Settings
import com.oneinch.on_chain_api.balance.Balance
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.repository.RealRepositoryManager
import com.oneinch.util.getLogger
import kotlinx.coroutines.delay
import org.springframework.stereotype.Component
import org.web3j.exceptions.MessageDecodingException
import org.web3j.tx.RawTransactionManager
import java.math.BigInteger
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@Component
class Sender(
    val settings: Settings,
    val rawTransactionManager: RawTransactionManager,
    val repository: RealRepositoryManager,
    val balance: Balance
) : ISender<Transaction> {

    @OptIn(ExperimentalTime::class)
    override suspend fun sendTransaction(tx: Transaction, from: TokenQuote, to: TokenQuote) {
        val newGasLimit = increaseGasLimit(tx.gasLimit)
        val newGasPrice = increaseGasPrice(tx.gasPrice)
        getLogger().info("Swapping, gasPrice: ${tx.gasPrice} gasLimit: $newGasLimit")
        getLogger().info("from: ${from.origin} to: ${to.origin}")
        try {
            val txHash = rawTransactionManager
                .sendTransaction(newGasPrice, newGasLimit, tx.address, tx.data, tx.value)
                .transactionHash
            repository.saveTransaction(from, to, newGasPrice, txHash, tx.maxSlippage, tx.requestTimestamp)
            getLogger().info(txHash)
            getLogger().info("---------------  WAITING FOR TRANSACTION SUCCEED  ---------------")
            delay(Duration.seconds(settings.waitTimeAfterSwap))
            balance.update(from)
            balance.update(to)
        } catch (e: MessageDecodingException) {
            getLogger().error("Transaction failed: ${e.stackTrace}")
        }
    }

    private fun increaseGasLimit(gasLimit: BigInteger): BigInteger {
        return (gasLimit.toDouble() * settings.increasedGasLimit).toBigDecimal().toBigInteger()
    }

    private fun increaseGasPrice(gasLimit: BigInteger): BigInteger {
        return (gasLimit.toDouble() * settings.increasedGasPrice).toBigDecimal().toBigInteger()
    }
}