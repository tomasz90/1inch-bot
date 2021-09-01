package com.oneinch.on_chain_api

import com.oneinch.InputConfig.INCREASED_GAS_LIMIT
import getLogger
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import org.springframework.stereotype.Component
import org.web3j.tx.RawTransactionManager
import java.math.BigInteger

interface ISender<T> {
    fun sendTransaction(t: T)
}

interface ITransaction

class Transaction(
    val gasPrice: BigInteger,
    val gasLimit: BigInteger,
    val value: BigInteger,
    val address: String,
    val data: String
) : ITransaction

class FakeTransaction : ITransaction

@Component
class Sender(private val rawTransactionManager: RawTransactionManager) : ISender<Transaction> {

    @DelicateCoroutinesApi
    override fun sendTransaction(t: Transaction) {
        val increasedGasLimit = increaseGasLimit(t.gasLimit)
        getLogger().info("Swapping, gasPrice: ${t.gasPrice} gasLimit: $increasedGasLimit")
        val tx = rawTransactionManager.sendTransaction(t.gasPrice, increasedGasLimit, t.address, t.data, t.value)
        getLogger().info("TxHash: ${tx.transactionHash}")
        GlobalScope.cancel("") // TODO: 01.09.2021 Check this
    }

    private fun increaseGasLimit(gasLimit: BigInteger): BigInteger {
        return (gasLimit.toDouble() * INCREASED_GAS_LIMIT).toBigDecimal().toBigInteger()
    }
}

@Component
class FakeSender : ISender<FakeTransaction> {
    override fun sendTransaction(t: FakeTransaction) {
    }
}