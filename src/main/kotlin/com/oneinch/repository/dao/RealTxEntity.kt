package com.oneinch.repository.dao

import com.oneinch.repository.dao.Status.FAIL
import java.util.*
import javax.persistence.*

@Entity
class RealTxEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    var id: Long = 0L,
    var sendTxTime: Date,
    var requestTimeS: Double,
    var txTimeS: Double,
    var chainId: Int,
    var hash: String,
    var from: String,
    var fromReadable: Double,
    var fromAmount: String,
    var to: String,
    var toReadable: Double,
    var toAmount: String,
    var gasPrice: Double,
    var minReturnAmount: String,
    var returnAmount: String,
    var advantage: Double,
    @Enumerated(EnumType.STRING)
    var status: Status
) {

    constructor() :
            this(
                0,
                Date(),
                0.0,
                0.0,
                0,
                "",
                "",
                0.0,
                "",
                "",
                0.0,
                "",
                0.0,
                "",
                "",
                0.0,
                FAIL
            )
}

enum class Status { PASSED, PARTIALLY, FAIL }
