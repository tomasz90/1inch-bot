package com.oneinch.api.blockchain.tx

import com.oneinch.config.Settings
import com.oneinch.util.GasPriceProvider
import com.oneinch.util.SlippageModifier
import java.math.BigInteger
import java.util.*

class Transaction private constructor(
    var gasPrice: BigInteger,
    var gasLimit: BigInteger,
    val value: BigInteger,
    val address: String,
    var data: String,
    val minReturnAmount: BigInteger,
    val advantage: Double,
    val requestTimestamp: Date
) : ITransaction {

    companion object {
        fun create(
            settings: Settings,
            slippageModifier: SlippageModifier,
            gasPriceProvider: GasPriceProvider,
            gasPrice: BigInteger,
            gasLimit: BigInteger,
            value: BigInteger,
            address: String,
            data: String,
            minReturnAmount: BigInteger,
            advantage: Double,
            requestTimestamp: Date
        ): Transaction {
            val tx = Transaction(gasPrice, gasLimit, value, address, data, minReturnAmount, advantage, requestTimestamp)
            tx.increaseGasLimit(settings)
            tx.increaseGasPrice(gasPriceProvider)
            tx.modifyData(slippageModifier)
            return tx
        }
    }

    private fun increaseGasLimit(settings: Settings) {
        gasLimit = (gasLimit.toDouble() * settings.increasedGasLimit).toBigDecimal().toBigInteger()
    }

    private fun increaseGasPrice(gasPriceProvider: GasPriceProvider) {
        gasPrice = gasPriceProvider.gasPrice.get().toBigInteger()
    }

    private fun modifyData(slippageModifier: SlippageModifier) {
        data = slippageModifier.modify(data, minReturnAmount)
    }
}