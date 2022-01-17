package com.oneinch.api.blockchain.tx

import com.oneinch.api.one_inch.api.data.SwapDto
import com.oneinch.loader.Settings
import com.oneinch.provider.GasPriceProvider
import com.oneinch.provider.SlippageProvider
import org.springframework.stereotype.Component
import java.math.BigInteger

@Component
class TransactionCreator(
    val settings: Settings,
    val slippageProvider: SlippageProvider,
    val gasPriceProvider: GasPriceProvider
) {

    fun create(dto: SwapDto, advantage: Double, requestDuration: Double): Transaction {
        val tx = dto.tx
        val newGasPrice = increaseGasPrice()
        val newGasLimit = BigInteger.valueOf(2000000)
        val value = tx.value
        val address = tx.to
        val minReturnAmount = dto.from.calcMinReturnAmountOfDifferentToken(dto.to.token)
        val newData = modifyData(tx.data, minReturnAmount)
        return Transaction(newGasPrice, newGasLimit, value, address, newData, minReturnAmount, advantage, requestDuration)
    }

    fun createBasic(dto: SwapDto):BasicTransaction {
        val tx = dto.tx
        val newGasPrice = increaseGasPrice()
        return BasicTransaction(newGasPrice, tx.gas, tx.value, tx.to, tx.data)
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