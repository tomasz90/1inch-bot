package com.oneinch.repository.dao

import com.oneinch.`object`.TokenQuote
import toOrigin
import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.TABLE
import javax.persistence.Id

@Entity
class FakeTokenQuoteEntity() {

    @GeneratedValue(strategy = TABLE)
    @Id
    var id: Long? = null
    lateinit var symbol: String
    lateinit var address: String
    lateinit var readable: BigDecimal

    constructor(symbol: String, address: String, readable: Double) : this() {
        this.symbol = symbol
        this.address = address
        this.readable = readable.toBigDecimal()
    }
}

fun FakeTokenQuoteEntity.toTokenQuote(): TokenQuote {
    return TokenQuote(symbol, address, toOrigin())
}

fun TokenQuote.toFakeTokenQuoteEntity(readable: Double): FakeTokenQuoteEntity {
    return FakeTokenQuoteEntity(this.symbol, this.address, readable)
}