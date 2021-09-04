package com.oneinch.on_chain_api.tx

import java.math.BigInteger

class Transaction(
    val gasPrice: BigInteger,
    val gasLimit: BigInteger,
    val value: BigInteger,
    val address: String,
    val data: String
) : ITransaction