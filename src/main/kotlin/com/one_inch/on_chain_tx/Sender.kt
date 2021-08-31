package com.one_inch.on_chain_tx

import com.one_inch.InputConfig.INCREASED_GAS_LIMIT
import getLogger
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import org.springframework.stereotype.Component
import org.web3j.tx.RawTransactionManager
import java.math.BigInteger

@Component
class Sender(private val rawTransactionManager: RawTransactionManager) {

    @DelicateCoroutinesApi
    fun sendTransaction(gasPrice: BigInteger, gasLimit: BigInteger, value: BigInteger, address: String, data: String) {
        val increasedGasLimit = increaseGasLimit(gasLimit)
        getLogger().info("Swapping, gasPrice: $gasPrice gasLimit: $increasedGasLimit")
        val tx = rawTransactionManager.sendTransaction(gasPrice, increasedGasLimit, address, data, value)
        getLogger().info("TxHash: ${tx.transactionHash}")
        GlobalScope.cancel("")
    }

    private fun increaseGasLimit(gasLimit: BigInteger): BigInteger {
        return (gasLimit.toDouble() * INCREASED_GAS_LIMIT).toBigDecimal().toBigInteger()
    }
}