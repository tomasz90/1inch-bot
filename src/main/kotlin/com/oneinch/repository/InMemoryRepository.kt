package com.oneinch.repository

import com.oneinch.one_inch_api.api.data.Token
import com.oneinch.one_inch_api.api.data.TokenQuote
import org.springframework.stereotype.Component

@Component
open class InMemoryRepository {

    val allBalance = mutableMapOf<TokenQuote, Boolean>()

    fun get(erc20: Token): TokenQuote {
        return allBalance
            .filterKeys { tokenQuote -> tokenQuote.token.address == erc20.address }
            .keys.first()
    }

    fun save(tokenQuote: TokenQuote) {
        allBalance[tokenQuote] = false
    }

    fun update(erc20: Token) {
        allBalance[get(erc20)] = true
    }

    fun needsRefresh(erc20: Token): Boolean {
        if(allBalance.isEmpty()) {
            return true
        }
        return allBalance
            .filterKeys { tokenQuote -> tokenQuote.token.address == erc20.address }
            .values.first()
    }
}