package com.oneinch.api.blockchain.tx

import java.math.BigInteger
import java.util.*

class Transaction (
    var gasPrice: BigInteger,
    var gasLimit: BigInteger,
    val value: BigInteger,
    val address: String,
    var data: String,
    val minReturnAmount: BigInteger,
    val advantage: Double,
    val requestTimestamp: Date
) : ITransaction