package com.oneinch.repository.crud

import com.oneinch.`object`.TokenQuote
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
open class InMemoryRepository {

    private val allBalance = mutableListOf<TokenQuote>()

    fun save(tokenQuote: TokenQuote) {
        allBalance.add(tokenQuote)
    }

    @Synchronized
    fun findByAddress(address: String): TokenQuote? {
        return allBalance.firstOrNull { it.token.address == address }
    }

    fun update(tokenQuote: TokenQuote) {
        val index = allBalance.indexOfFirst { it.token.address == tokenQuote.token.address }
        if (index > -1) {
            allBalance.removeAt(index)
        }
        allBalance.add(tokenQuote)
    }

    fun getUsdValue(): Double {
        return allBalance.sumOf { it.usdValue }
    }

    fun getAnyTokenWithBalance(amount: Double): TokenQuote {
        return allBalance.first { it.usdValue > amount }
    }
}