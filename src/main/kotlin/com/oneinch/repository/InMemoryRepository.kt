package com.oneinch.repository

import com.oneinch.`object`.TokenQuote
import org.springframework.stereotype.Component
import java.math.BigInteger

@Component
open class InMemoryRepository {

    private val allBalance = mutableListOf<TokenQuote>()
    private var allBalanceReadable: Double = 0.0

    fun save(tokenQuote: TokenQuote) {
        allBalance.add(tokenQuote)
        allBalanceReadable += tokenQuote.calcReadable()
    }

    @Synchronized  // TODO: 13.09.2021 if exception occur once again delete it
    fun findByAddress(address: String): TokenQuote? {
        return allBalance.firstOrNull { it.token.address == address }
    }

    fun update(tokenQuote: TokenQuote) {
        val index = allBalance.indexOfFirst { it.token.address == tokenQuote.token.address }
        if (index > -1) {
            allBalance.removeAt(index)
        }
        save(tokenQuote)
    }

    fun getAllBalanceReadable(): Double {
        return allBalanceReadable
    }
}