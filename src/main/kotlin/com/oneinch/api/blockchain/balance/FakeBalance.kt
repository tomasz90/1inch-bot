package com.oneinch.api.blockchain.balance

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.repository.FakeRepositoryManager
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("fakeAccount")
class FakeBalance(val repository: FakeRepositoryManager) : IBalance {

    override fun getERC20(erc20: Token): TokenQuote? {
        return repository.getBalance(erc20)
    }

    override fun getUsdValue(): Double {
        return repository.getUsdValue()
    }
}