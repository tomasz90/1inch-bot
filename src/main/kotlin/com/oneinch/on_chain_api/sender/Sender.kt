package com.oneinch.on_chain_api.sender

import com.oneinch.`object`.TokenQuote
import com.oneinch.config.Settings
import com.oneinch.getLogger
import com.oneinch.on_chain_api.balance.Balance
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.repository.RealRepositoryManager
import kotlinx.coroutines.delay
import org.springframework.stereotype.Component
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
    override suspend fun sendTransaction(t: Transaction, from: TokenQuote, to: TokenQuote, string: String) {
        val newGasLimit = increaseGasLimit(t.gasLimit)
        val newGasPrice = increaseGasPrice(t.gasPrice)
        getLogger().info("Swapping, gasPrice: ${t.gasPrice} gasLimit: $newGasLimit")
        getLogger().info("from: ${from.origin} to: ${to.origin}")
        val txHash = rawTransactionManager
            .sendTransaction(newGasPrice, newGasLimit, t.address, t.data, t.value)
            .transactionHash
        getLogger().info("SWAP_ID: $string")
        repository.saveTransaction(from, to, newGasPrice, txHash, t.maxSlippage)
        getLogger().info(txHash)
        getLogger().info("---------------  WAITING FOR TRANSACTION SUCCEED  ---------------")
        delay(Duration.seconds(120))
        balance.update(from)
        balance.update(to)
    }

    private fun increaseGasLimit(gasLimit: BigInteger): BigInteger {
        return (gasLimit.toDouble() * settings.increasedGasLimit).toBigDecimal().toBigInteger()
    }

    private fun increaseGasPrice(gasLimit: BigInteger): BigInteger {
        return (gasLimit.toDouble() * settings.increasedGasPrice).toBigDecimal().toBigInteger()
    }
}