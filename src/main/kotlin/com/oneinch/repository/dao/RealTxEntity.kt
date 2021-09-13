package com.oneinch.repository.dao

import org.hibernate.annotations.CreationTimestamp
import java.math.BigInteger
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class RealTxEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    var id: Long = 0L,
    @CreationTimestamp
    var date: Date = Date(),
    var requestTimeStamp: Date,
    var chainId: Int,
    var hash: String,
    var fromSymbol: String,
    var fromAddress: String,
    var fromReadable: Double,
    var fromAmount: String,
    var toSymbol: String,
    var toAddress: String,
    var toReadable: Double,
    var toAmount: String,
    var gasPrice: BigInteger,
    var maxSlippage: Double
) {

    constructor() :
            this(
                0,
                Date(),
                Date(),
                0,
                "",
                "",
                "",
                0.0,
                "",
                "",
                "",
                0.0,
                "",
                BigInteger("0"),
                0.0
            )
}