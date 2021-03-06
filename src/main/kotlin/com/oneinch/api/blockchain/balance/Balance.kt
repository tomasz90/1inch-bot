package com.oneinch.api.blockchain.balance

import com.oneinch.`object`.Chain
import com.oneinch.`object`.CoinQuote
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.repository.crud.InMemoryRepository
import com.oneinch.util.getLogger
import org.springframework.stereotype.Component
import org.web3j.contracts.eip20.generated.ERC20.load
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.tx.ClientTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger

@Component
class Balance(val web3j: JsonRpc2_0Web3j, val myAddress: String, val repository: InMemoryRepository, val chain: Chain) {

    fun getERC20(erc20: Token): TokenQuote? {
        var tokenQuote = repository.findByAddress(erc20.address)
        if (tokenQuote == null) {
            tokenQuote = getFromChain(erc20.address)
            if (tokenQuote != null) {
                repository.save(tokenQuote)
            }
        }
        return tokenQuote
    }

    fun getUsdValue(): Double {
        return repository.getUsdValue()
    }

    fun getCoin(): CoinQuote? {
        val quote: BigInteger
        try {
            quote = web3j.ethGetBalance(myAddress, LATEST).send().balance
        } catch (e: Exception) {
            getLogger().info("Exception during getting balance: ${e.message}")
            return null
        }
        return CoinQuote(chain.coin, quote)
    }

    fun getAnyTokenWithBalance(amount: Double) : TokenQuote {
        return repository.getAnyTokenWithBalance(amount)
    }

    fun updateAll() {
        chain.tokens
            .mapNotNull { getFromChain(it.address) }
            .forEach { repository.update(it) }
    }

    private fun getFromChain(address: String): TokenQuote? {
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
        val token = chain.tokens.first { address == it.address }
        return TokenQuote(token, quote)
    }
}

