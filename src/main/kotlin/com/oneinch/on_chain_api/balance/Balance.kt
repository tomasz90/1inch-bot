package com.oneinch.on_chain_api.balance

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.repository.InMemoryRepository
import com.oneinch.util.getLogger
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

    override fun getERC20(erc20: Token): TokenQuote? {
        var tokenQuote = repository.findByAddress(erc20.address)
        if (tokenQuote == null) {
            tokenQuote = getFromChain(erc20.symbol, erc20.address)
            if (tokenQuote != null) {
                repository.save(tokenQuote)
            }
        }
        return tokenQuote
    }

    fun update(tokenQuote: TokenQuote) {
        val newTokenQuote = getFromChain(tokenQuote.symbol, tokenQuote.address)
        if (newTokenQuote != null) {
            repository.update(newTokenQuote)
        }
    }

    private fun getFromChain(symbol: String, address: String): TokenQuote? {
        val txManager = ClientTransactionManager(web3j, myAddress)
        val contract = load(address, web3j, txManager, DefaultGasProvider())
        val quote: BigInteger
        try {
            quote = contract.balanceOf(myAddress).send()
        } catch (e: Exception) {
            getLogger().info("Exception during getting balance: ${e.message}")
            return null
        }
        getLogger().info("Getting balance from chain: $quote")
        return TokenQuote(symbol, address, quote)
    }
}

