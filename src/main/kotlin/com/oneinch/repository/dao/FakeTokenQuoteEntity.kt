package com.oneinch.repository.dao

import com.oneinch.`object`.TokenQuote
import java.math.BigInteger
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class FakeTokenQuoteEntity(var symbol: String, @Id var address: String, var readable: Double, var origin: String) {

    constructor() : this("", "", 0.0, "0")
}

fun FakeTokenQuoteEntity.toTokenQuote(): TokenQuote {
    return TokenQuote(symbol, address, BigInteger(origin))
}