package com.oneinch.on_chain_api.sender

import com.oneinch.`object`.TokenQuote
import com.oneinch.config.Settings
import com.oneinch.getLogger
import com.oneinch.on_chain_api.balance.Balance
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.one_inch_api.requester.EnterSwap.canEnter
import com.oneinch.repository.RealRepositoryManager
import org.springframework.stereotype.Component
import org.web3j.tx.RawTransactionManager
import java.math.BigInteger
import java.util.concurrent.TimeUnit

@Component
class Sender(
    val settings: Settings,
    val rawTransactionManager: RawTransactionManager,
    val repository: RealRepositoryManager,
    val balance: Balance
) : ISender<Transaction> {

    override fun sendTransaction(t: Transaction, from: TokenQuote, to: TokenQuote) {
        val newGasLimit = increaseGasLimit(t.gasLimit)
        val newGasPrice = increaseGasPrice(t.gasPrice)
        getLogger().info("Swapping, gasPrice: ${t.gasPrice} gasLimit: $newGasLimit")
        getLogger().info("from: ${from.origin} to: ${to.origin}")
        val txHash = rawTransactionManager
            .sendTransaction(newGasPrice, newGasLimit, t.address, t.data, t.value)
            .transactionHash
        repository.saveTransaction(from, to, newGasPrice, txHash, t.maxSlippage)
        getLogger().info(txHash)
        getLogger().info("WAITING FOR TRANSACTION SUCCEED")
        TimeUnit.SECONDS.sleep(120)
        balance.update(from)
        balance.update(to)
        canEnter = true
    }

    private fun increaseGasLimit(gasLimit: BigInteger): BigInteger {
        return (gasLimit.toDouble() * settings.increasedGasLimit).toBigDecimal().toBigInteger()
    }

    private fun increaseGasPrice(gasLimit: BigInteger): BigInteger {
        return (gasLimit.toDouble() * settings.increasedGasPrice).toBigDecimal().toBigInteger()
    }
}