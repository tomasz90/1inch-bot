package com.oneinch.repository.dao

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import java.math.BigInteger
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class FakeTokenQuoteEntity(var chainId: Int, var symbol: String, @Id var address: String, var readable: Double, var origin: String, var decimals: BigInteger) {

    constructor() : this(0, "", "", 0.0, "0", BigInteger.valueOf(0))
}

fun FakeTokenQuoteEntity.toTokenQuote(): TokenQuote {
    val token = Token(symbol, address, decimals)
    return TokenQuote(token, BigInteger(origin))
}