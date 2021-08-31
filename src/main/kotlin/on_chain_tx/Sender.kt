package on_chain_tx

import Config.CHAIN
import Config.INCREASED_GAS_LIMIT
import getLogger
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import java.math.BigInteger

class Sender {

    private val web3service = HttpService(CHAIN.rpc)
    private val web3j = JsonRpc2_0Web3j(web3service)
    private val credentials = WalletManager().openWallet()
    private val manager = RawTransactionManager(web3j, credentials, CHAIN.id.toLong())

    fun sendTransaction(gasPrice: BigInteger, gasLimit: BigInteger, value: BigInteger, address: String, data: String) {
        val increasedGasLimit = increaseGasLimit(gasLimit)
        getLogger().info("Swapping, gasPrice: $gasPrice gasLimit: $increasedGasLimit")
        val tx = manager.sendTransaction(gasPrice, increasedGasLimit, address, data, value)
        getLogger().info("TxHash: ${tx.transactionHash}")
    }

    private fun increaseGasLimit(gasLimit: BigInteger): BigInteger {
        return (gasLimit.toDouble() * INCREASED_GAS_LIMIT).toBigDecimal().toBigInteger()
    }
}