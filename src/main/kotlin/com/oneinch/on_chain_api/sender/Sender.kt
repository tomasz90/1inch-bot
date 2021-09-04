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

@Component
class Sender(
    val settings: Settings,
    val rawTransactionManager: RawTransactionManager,
    val repository: RealRepositoryManager,
    val inMemoryRepository: InMemoryRepository
) : ISender<Transaction> {

    override fun sendTransaction(t: Transaction, from: TokenQuote, to: TokenQuote) {
        val newGasLimit = increaseGasLimit(t.gasLimit)
        getLogger().info("Swapping, gasPrice: ${t.gasPrice} gasLimit: $newGasLimit")
        val txHash = rawTransactionManager
            .sendTransaction(t.gasPrice, newGasLimit, t.address, t.data, t.value)
            .transactionHash
        repository.saveTransaction(from, to, t, txHash)
        inMemoryRepository.update(from.symbol)
    }

    private fun increaseGasLimit(gasLimit: BigInteger): BigInteger {
        return (gasLimit.toDouble() * settings.increasedGasLimit).toBigDecimal().toBigInteger()
    }
}