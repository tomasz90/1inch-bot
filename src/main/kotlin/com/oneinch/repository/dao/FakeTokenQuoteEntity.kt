package com.oneinch.repository.dao

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import java.math.BigDecimal
import java.math.BigInteger
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.TABLE
import javax.persistence.Id
import kotlin.properties.Delegates

@Entity
class FakeTokenQuoteEntity() {

    @GeneratedValue(strategy = TABLE)
    @Id
    var id: Long? = null
    lateinit var symbol: String
    lateinit var address: String
    lateinit var readable: BigDecimal
    lateinit var decimal: Integer

    constructor(symbol: String, address: String, readable: Double, decimal: Int): this() {
        this.symbol = symbol
        this.address = address
        this.readable = readable.toBigDecimal()
        this.decimal = Integer(decimal)
    }
}

fun FakeTokenQuoteEntity.toTokenQuote(): TokenQuote {
    val token = Token(symbol, address, decimal.toInt())
    return TokenQuote(token, readable.toDouble())
}