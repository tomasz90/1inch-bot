package com.oneinch.repository.dao

import com.oneinch.`object`.TokenQuote
import toOrigin
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class FakeTokenQuoteEntity(var symbol: String, @Id var address: String, var readable: Double) {

    constructor() : this("", "", 0.0)
}

fun FakeTokenQuoteEntity.toTokenQuote(): TokenQuote {
    return TokenQuote(symbol, address, toOrigin())
}

fun TokenQuote.toFakeTokenQuoteEntity(readable: Double): FakeTokenQuoteEntity {
    return FakeTokenQuoteEntity(this.symbol, this.address, readable)
}