package com.oneinch.repository

import com.oneinch.`object`.TokenQuote
import org.springframework.stereotype.Component
import java.math.BigInteger

@Component
open class InMemoryRepository {

    val allBalance = mutableMapOf<String, TokenQuote>()


    fun save(tokenQuote: TokenQuote) {
        allBalance.put(tokenQuote.address, tokenQuote)
    }

    fun findByAddress(address: String): TokenQuote? {
        return allBalance.getOrDefault(address, null)
    }

    fun update(tokenQuote: TokenQuote) {
        val x = findByAddress(tokenQuote.address) // TODO: 05.09.2021 to remove?
        allBalance.replace(tokenQuote.address, tokenQuote)
    }
}

class InMemoryToken(symbol: String, origin: BigInteger)