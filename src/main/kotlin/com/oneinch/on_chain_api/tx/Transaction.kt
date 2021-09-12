package com.oneinch.on_chain_api.tx

import java.math.BigInteger
import java.util.*

class Transaction(
    val gasPrice: BigInteger,
    val gasLimit: BigInteger,
    val value: BigInteger,
    val address: String,
    val data: String,
    val maxSlippage: Double,
    val requestTimestamp: Date
) : ITransaction