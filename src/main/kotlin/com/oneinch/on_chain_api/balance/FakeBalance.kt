package com.oneinch.on_chain_api.balance

import com.oneinch.repository.Repository
import com.oneinch.one_inch_api.api.data.Token
import com.oneinch.one_inch_api.api.data.TokenQuote
import org.springframework.stereotype.Component

@Component
class FakeBalance(val repository: Repository) : IBalance {

    override fun getERC20(erc20: Token): TokenQuote {
        return repository.getBalance(erc20)
    }
}