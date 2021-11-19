package com.oneinch.api.paraswap.api.data

class TransactionResponse(
    val from: String,
    val to: String,
    val value: String,
    val data: String,
    val gasPrice: String,
    val chainId: Int,
    val gas: String
)

class TransactionResponseIgnoreChecks()