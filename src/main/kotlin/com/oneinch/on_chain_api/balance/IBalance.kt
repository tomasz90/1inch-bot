package com.oneinch.on_chain_api.balance

import com.oneinch.one_inch_api.api.data.Token
import com.oneinch.one_inch_api.api.data.TokenQuote

interface IBalance {

    fun getERC20(erc20: Token): TokenQuote

    fun refresh(erc20: Token, boolean: Boolean)


    fun MutableMap<TokenQuote, Boolean>.filterBalance(erc20: Token): Map<TokenQuote, Boolean> {
        return this.filterKeys { tokenQuote -> tokenQuote.token.address == erc20.address }
    }
}