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
        allBalance[tokenQuote] = false
    }

    fun update(symbol: String) {
        allBalance[get(symbol)] = true
    }

    fun needsRefresh(symbol: String): Boolean {
        if(allBalance.isEmpty()) {
            return true
        }
        return allBalance
            .filterKeys { tokenQuote -> tokenQuote.symbol == symbol }
            .values.first()
    }
}