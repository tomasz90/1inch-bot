package on_chain_tx

import org.web3j.contracts.eip20.generated.ERC20.load
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName.LATEST
import org.web3j.tx.ClientTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import java.math.BigInteger

class BalanceProvider(private val web3j: Web3j, private val address: String) {

    fun getBalance(): BigInteger {
        return web3j.ethGetBalance(address, LATEST).send().balance
    }

    fun getBalance(erc20Address: String): BigInteger {
        val txManager = ClientTransactionManager(web3j, address)
        val contract = load(erc20Address, web3j, txManager, DefaultGasProvider())
        return contract.balanceOf(address).send()
    }
}