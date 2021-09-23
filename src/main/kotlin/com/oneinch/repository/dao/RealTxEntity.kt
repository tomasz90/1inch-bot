package com.oneinch.repository.dao

import com.oneinch.repository.dao.Status.FAIL
import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

@Entity
class RealTxEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    var id: Long = 0L,
    var requestTimeStamp: Date,
    var sendTxTimeStamp: Date,
    var settleTime: Long,
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
    var gasPrice: String,
    var minReturnAmount: String,
    var advantage: Double,
    @Enumerated(EnumType.STRING)
    var status: Status
) {

    constructor() :
            this(
                0,
                Date(),
                Date(),
                0,
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
                "",
                "",
                0.0,
                FAIL
            )
}

enum class Status { PASSED, PARTIALLY, FAIL }
