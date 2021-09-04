package com.oneinch.on_chain_api.sender

import com.oneinch.config.Settings
import com.oneinch.on_chain_api.balance.IBalance
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.one_inch_api.api.data.TokenQuote
import getLogger
import org.springframework.stereotype.Component
import org.web3j.tx.RawTransactionManager
import java.math.BigInteger

@Component
class Sender(val rawTransactionManager: RawTransactionManager, val balance: IBalance, val settings: Settings) :
    ISender<Transaction> {

    override fun sendTransaction(t: Transaction, from: TokenQuote) {
        val increasedGasLimit = increaseGasLimit(t.gasLimit)
        getLogger().info("Swapping, gasPrice: ${t.gasPrice} gasLimit: $increasedGasLimit")
        val tx = rawTransactionManager.sendTransaction(t.gasPrice, increasedGasLimit, t.address, t.data, t.value)
        getLogger().info("TxHash: ${tx.transactionHash}")
        balance.refresh(from.token, true)
    }

    private fun increaseGasLimit(gasLimit: BigInteger): BigInteger {
        return (gasLimit.toDouble() * settings.increasedGasLimit).toBigDecimal().toBigInteger()
    }
}