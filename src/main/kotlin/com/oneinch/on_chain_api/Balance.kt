package com.oneinch.on_chain_api

import com.oneinch.oneinch_api.api.data.Token
import com.oneinch.oneinch_api.api.data.TokenQuote
import org.springframework.stereotype.Component
import org.web3j.contracts.eip20.generated.ERC20.load
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.tx.ClientTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger

interface IBalance {

    fun getERC20(erc20: Token): TokenQuote

    fun refresh(erc20: Token, boolean: Boolean)
}

@Component
class Balance(private val web3j: JsonRpc2_0Web3j, private val myAddress: String) : IBalance {

    private val allBalance: MutableMap<TokenQuote, Boolean> = mutableMapOf()

    fun get(): BigInteger {
        return web3j.ethGetBalance(myAddress, LATEST).send().balance
    }

    override fun getERC20(erc20: Token): TokenQuote {
        return if (allBalance.isEmpty() || allBalance.filterBalance(erc20).values.first()) {
            val txManager = ClientTransactionManager(web3j, myAddress)
            val contract = load(erc20.address, web3j, txManager, DefaultGasProvider())
            val quote = contract.balanceOf(myAddress).send()
            val tokenQuote = TokenQuote(erc20, quote)
            allBalance[tokenQuote] = false
            tokenQuote
        } else {
            allBalance.filterBalance(erc20).keys.first()
        }
    }

    override fun refresh(erc20: Token, boolean: Boolean) {
        allBalance[allBalance.filterBalance(erc20).keys.first()] = boolean
    }
}

fun MutableMap<TokenQuote, Boolean>.filterBalance(erc20: Token): Map<TokenQuote, Boolean> {
    return this.filterKeys { tokenQuote -> tokenQuote.token.address == erc20.address }
}

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