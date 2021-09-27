package com.oneinch.api.blockchain.tx

import java.math.BigInteger

open class BasicTransaction(
    var gasPrice: BigInteger,
    var gasLimit: BigInteger,
    val value: BigInteger,
    val address: String,
    var data: String,
)

class Transaction(
    gasPrice: BigInteger,
    gasLimit: BigInteger,
    value: BigInteger,
    address: String,
    data: String,
    val minReturnAmount: BigInteger,
    val advantage: Double,
    val requestDuration: Double
) : BasicTransaction(
    gasPrice,
    gasLimit,
    value,
    address,
    data
), ITransaction