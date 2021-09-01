package com.oneinch.on_chain_api

import org.springframework.stereotype.Component
import org.web3j.contracts.eip20.generated.ERC20.load
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.tx.ClientTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger

interface IBalance{

    fun getERC20(erc20Address: String): BigInteger
}

@Component
class Balance(private val web3j: JsonRpc2_0Web3j, private val myAddress: String) : IBalance {

    fun get(): BigInteger {
        return web3j.ethGetBalance(myAddress, LATEST).send().balance
    }

    override fun getERC20(erc20Address: String): BigInteger {
        val txManager = ClientTransactionManager(web3j, myAddress)
        val contract = load(erc20Address, web3j, txManager, DefaultGasProvider())
        return contract.balanceOf(myAddress).send()
    }

}

@Component
class FakeBalance() : IBalance {
    override fun getERC20(erc20Address: String): BigInteger {
        TODO("Not yet implemented")
    }
}