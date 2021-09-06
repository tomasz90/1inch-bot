package com.oneinch.repository.dao

import com.oneinch.`object`.TokenQuote
import java.math.BigInteger
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class FakeTokenQuoteEntity(var symbol: String, @Id var address: String, var readable: Double, var origin: BigInteger) {

    constructor() : this("", "", 0.0, BigInteger("0"))
}

fun FakeTokenQuoteEntity.toTokenQuote(): TokenQuote {
    return TokenQuote(symbol, address, origin)
}

fun TokenQuote.toFakeTokenQuoteEntity(readable: Double): FakeTokenQuoteEntity {
    return FakeTokenQuoteEntity(this.symbol, this.address, readable, origin)
}