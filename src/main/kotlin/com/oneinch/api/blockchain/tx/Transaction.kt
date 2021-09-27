package com.oneinch.api.blockchain.tx

import java.math.BigInteger
import java.util.*

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
    val requestTimestamp: Date
) : BasicTransaction(
    gasPrice,
    gasLimit,
    value,
    address,
    data
), ITransaction