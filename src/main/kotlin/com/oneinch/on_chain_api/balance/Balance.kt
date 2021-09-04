package com.oneinch.on_chain_api.balance

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.repository.InMemoryRepository
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
        return if (repository.needsRefresh(erc20.symbol)) {
            val tokenQuote = getFromChain(erc20)
            repository.save(tokenQuote)
            tokenQuote
        } else {
            repository.get(erc20.symbol)
        }
    }

    private fun getFromChain(erc20: Token): TokenQuote {
        val txManager = ClientTransactionManager(web3j, myAddress)
        val contract = load(erc20.address, web3j, txManager, DefaultGasProvider())
        val quote = contract.balanceOf(myAddress).send()
        return TokenQuote(erc20.symbol, erc20.address, quote)
    }
}

