package com.oneinch.api.blockchain.tx

import com.oneinch.loaders.Settings
import com.oneinch.provider.GasPriceProvider
import com.oneinch.provider.SlippageProvider
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.util.*

@Component
class TransactionCreator(
    val settings: Settings,
    val slippageProvider: SlippageProvider,
    val gasPriceProvider: GasPriceProvider
) {

    fun create(gasLimit: BigInteger,
               value: BigInteger,
               address: String,
               data: String,
               minReturnAmount: BigInteger,
               advantage: Double,
               requestTimestamp: Date
    ): Transaction {
        val newGasLimit = increaseGasLimit(gasLimit)
        val newGasPrice = increaseGasPrice()
        val newData = modifyData(data, minReturnAmount)
        return Transaction(newGasPrice, newGasLimit, value, address, newData, minReturnAmount, advantage, requestTimestamp)
    }

    private fun increaseGasLimit(gasLimit: BigInteger): BigInteger {
        return (gasLimit.toDouble() * settings.increasedGasLimit).toBigDecimal().toBigInteger()
    }

    private fun increaseGasPrice(): BigInteger {
        return gasPriceProvider.gasPrice.get().toBigInteger()
    }

    private fun modifyData(data: String, minReturnAmount: BigInteger): String {
        return slippageProvider.modify(data, minReturnAmount)
    }
}