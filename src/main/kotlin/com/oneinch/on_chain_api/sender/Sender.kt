package com.oneinch.on_chain_api.sender

import com.oneinch.`object`.Chain
import com.oneinch.`object`.TokenQuote
import com.oneinch.config.Settings
import com.oneinch.on_chain_api.balance.Balance
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.repository.RealRepositoryManager
import com.oneinch.repository.dao.Passed
import com.oneinch.repository.dao.Passed.FAIL
import com.oneinch.repository.dao.Passed.PASSED
import com.oneinch.util.Utils
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
    val balance: Balance,
    val utils: Utils,
    val chain: Chain
) : ISender<Transaction> {

    override suspend fun sendTransaction(tx: Transaction, from: TokenQuote, to: TokenQuote) {
        val newGasLimit = increaseGasLimit(tx.gasLimit)
        val newGasPrice = increaseGasPrice(tx.gasPrice)
        utils.logSwapInfo(from, to)
        try {
            val txHash = rawTransactionManager
                .sendTransaction(newGasPrice, newGasLimit, tx.address, tx.data, tx.value)
                .transactionHash
            getLogger().info(txHash)
            getLogger().info("---------------  WAITING FOR TRANSACTION SUCCEED  ---------------")
            delay(settings.waitTimeAfterSwap * 1000)
            balance.updateAll()
            if(balance.getERC20(from.token)?.origin == from.origin) {
                repository.saveTransaction(from, to, newGasPrice, txHash, tx.maxSlippage, tx.advantage, tx.requestTimestamp, FAIL)
            } else {
                repository.saveTransaction(from, to, newGasPrice, txHash, tx.maxSlippage, tx.advantage, tx.requestTimestamp, PASSED)
            }
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