package com.oneinch.repository

import com.oneinch.`object`.TokenQuote
import org.springframework.stereotype.Component

@Component
open class InMemoryRepository {

    val allBalance = mutableMapOf<TokenQuote, Boolean>()

    fun get(symbol: String): TokenQuote {
        return allBalance
            .filterKeys { tokenQuote -> tokenQuote.symbol == symbol }
            .keys.first()
    }

    fun save(tokenQuote: TokenQuote) {
        val x = allBalance
            .filterKeys { token -> tokenQuote.symbol == token.symbol }
            .values.firstOrNull()
        if (x == null) {
            allBalance[tokenQuote] = false
        }
    }

    fun update(tokenQuote: TokenQuote) {
        val x = allBalance
            .filterKeys { token -> tokenQuote.symbol == token.symbol }
            .values.firstOrNull()
        if (x == null) {
            allBalance[tokenQuote] = true
        } else {
            allBalance[allBalance.filterKeys { it.symbol == tokenQuote.symbol }.entries.first().key] = true
        }
    }

    fun needsRefresh(symbol: String): Boolean {
        return allBalance
            .filterKeys { tokenQuote -> tokenQuote.symbol == symbol }
            .values.firstOrNull() ?: true
    }
}