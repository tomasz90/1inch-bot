package com.oneinch.on_chain_api.balance

import com.oneinch.one_inch_api.api.data.Token
import com.oneinch.one_inch_api.api.data.TokenQuote
import org.springframework.stereotype.Component

@Component
class FakeBalance : IBalance {

    // TODO: 02.09.2021 DB here is needed

    override fun getERC20(erc20: Token): TokenQuote {
        TODO("Not yet implemented")
    }

    override fun refresh(erc20: Token, boolean: Boolean) {
        TODO("Not yet implemented")
    }
}