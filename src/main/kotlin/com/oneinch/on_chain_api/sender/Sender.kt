package com.oneinch.on_chain_api.sender

import com.oneinch.config.Settings
import com.oneinch.repository.FakeRepositoryManager
import com.oneinch.repository.InMemoryRepository
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.`object`.TokenQuote
import com.oneinch.repository.IRepositoryManager
import com.oneinch.repository.RealRepositoryManager
import getLogger
import org.springframework.stereotype.Component
import org.web3j.tx.RawTransactionManager
import java.math.BigInteger
import java.util.concurrent.TimeUnit

@Component
class Sender(
    val settings: Settings,
    val rawTransactionManager: RawTransactionManager,
    val repository: RealRepositoryManager,
    val inMemoryRepository: InMemoryRepository
) : ISender<Transaction> {

    override fun sendTransaction(t: Transaction, from: TokenQuote, to: TokenQuote) {
        val newGasLimit = increaseGasLimit(t.gasLimit)
        val newGasPrice = increaseGasPrice(t.gasPrice)
        getLogger().info("Swapping, gasPrice: ${t.gasPrice} gasLimit: $newGasLimit")
        getLogger().info("from: ${from.origin} to: ${to.origin}")
        val txHash = rawTransactionManager
            .sendTransaction(newGasPrice, newGasLimit, t.address, t.data, t.value)
            .transactionHash
        repository.saveTransaction(from, to, t, txHash)
        getLogger().info(txHash)
        inMemoryRepository.update(from)
        inMemoryRepository.update(to)

        getLogger().info("WAITING FOR TRANSACTION SUCCEED")
        TimeUnit.SECONDS.sleep(30)
    }

    private fun increaseGasLimit(gasLimit: BigInteger): BigInteger {
        return (gasLimit.toDouble() * settings.increasedGasLimit).toBigDecimal().toBigInteger()
    }

    private fun increaseGasPrice(gasLimit: BigInteger): BigInteger {
        return (gasLimit.toDouble() * settings.increasedGasPrice).toBigDecimal().toBigInteger()
    }
}