package com.oneinch.on_chain_tx

import org.springframework.stereotype.Component
import org.web3j.contracts.eip20.generated.ERC20.load
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.tx.ClientTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger

interface IBalanceProvider{

    fun getAllERC20Balance()
}

@Component
class FakeBalanceProvider() : IBalanceProvider {
    override fun getAllERC20Balance() {
        TODO("Not yet implemented")
    }
}

@Component
class BalanceProvider(private val web3j: JsonRpc2_0Web3j, private val myAddress: String) : IBalanceProvider {

    fun getBalance(): BigInteger {
        return web3j.ethGetBalance(myAddress, LATEST).send().balance
    }

    fun getERC20Balance(erc20Address: String): BigInteger {
        val txManager = ClientTransactionManager(web3j, myAddress)
        val contract = load(erc20Address, web3j, txManager, DefaultGasProvider())
        return contract.balanceOf(myAddress).send()
    }

    override fun getAllERC20Balance() {
        TODO("Not yet implemented")
    }
}