package com.oneinch.repository.dao

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import java.math.BigInteger
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class FakeTokenQuoteEntity(var chainId: Int, var symbol: String, @Id var address: String, var readable: Double) {

    constructor() : this(0, "", "", 0.0)
}

fun FakeTokenQuoteEntity.toTokenQuote(chain: Chain): TokenQuote {
    val token = chain.tokens.first { it.address == this.address }
    val origin = token.decimals.toDouble() * readable
    return TokenQuote(token, origin.toBigDecimal().toBigInteger())
}