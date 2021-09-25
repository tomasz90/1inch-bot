package com.oneinch.api.blockchain.balance

import com.oneinch.repository.FakeRepositoryManager
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import org.springframework.stereotype.Component

@Component
class FakeBalance(val repository: FakeRepositoryManager) : IBalance {

    override fun getERC20(erc20: Token): TokenQuote? {
        return repository.getBalance(erc20)
    }
}