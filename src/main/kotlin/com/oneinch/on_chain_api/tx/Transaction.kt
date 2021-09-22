package com.oneinch.on_chain_api.tx

import com.oneinch.config.Settings
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
            tx.increaseGasPrice(settings)
            tx.modifyData(slippageModifier)
            return tx
        }
    }

    private fun increaseGasLimit(settings: Settings) {
        gasLimit = (gasLimit.toDouble() * settings.increasedGasLimit).toBigDecimal().toBigInteger()
    }

    private fun increaseGasPrice(settings: Settings) {
        gasPrice = (gasPrice.toDouble() * settings.increasedGasPrice).toBigDecimal().toBigInteger()
    }

    private fun modifyData(slippageModifier: SlippageModifier) {
        data = slippageModifier.modify(data, minReturnAmount)
    }
}