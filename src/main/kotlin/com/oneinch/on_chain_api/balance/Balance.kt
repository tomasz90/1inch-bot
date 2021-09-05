package com.oneinch.on_chain_api.balance

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.repository.InMemoryRepository
import getLogger
import org.springframework.stereotype.Component
import org.web3j.contracts.eip20.generated.ERC20.load
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.tx.ClientTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger

@Component
class Balance(val web3j: JsonRpc2_0Web3j, val myAddress: String, val repository: InMemoryRepository) : IBalance {

    fun get(): BigInteger {
        return web3j.ethGetBalance(myAddress, LATEST).send().balance
    }

    override fun getERC20(erc20: Token): TokenQuote {
        var x = repository.findByAddress(erc20.address)
        if (x == null) {
            x = getFromChain(erc20.symbol, erc20.address)
            repository.save(x)
        }
        return x
    }

    fun update(tokenQuote: TokenQuote) {
        val x = getFromChain(tokenQuote.symbol, tokenQuote.address)
        repository.update(x)
    }

    private fun getFromChain(symbol: String, address: String): TokenQuote {
        val txManager = ClientTransactionManager(web3j, myAddress)
        val contract = load(address, web3j, txManager, DefaultGasProvider())
        val quote = contract.balanceOf(myAddress).send() //todo: catch sockettimeout excepion
        getLogger().info("Getting balance from chain: $quote")
        return TokenQuote(symbol, address, quote)
    }
}

