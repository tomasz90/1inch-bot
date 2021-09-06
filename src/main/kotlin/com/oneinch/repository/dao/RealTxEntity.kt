package com.oneinch.repository.dao

import java.math.BigInteger
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class RealTxEntity(
    var chainId: Int,
    var hash: String,
    var gasPrice: BigInteger,
    var maxSlippage: Double,
    var fromAddress: String,
    var fromAmount: String,
    var fromReadable: Double,
    var toAddress: String,
    var toAmount: String,
    var toReadable: Double,
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    var id: Long = 0L
) {


    constructor() :
            this(0,
                "",
                BigInteger("0"),
                0.0,
                "",
                "",
                0.0,
                "",
                "",
                0.0
            )
}