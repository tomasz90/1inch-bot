package on_chain_tx

import Config.CHAIN
import getLogger
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import java.math.BigInteger

object Sender {

    private val web3service = HttpService(CHAIN.rpc)
    private val web3 = JsonRpc2_0Web3j(web3service)
    private val credentials = WalletManager().openWallet()
    private val manager = RawTransactionManager(web3, credentials, CHAIN.id.toLong())

    fun sendTransaction(gasPrice: BigInteger, gasLimit: BigInteger, value: BigInteger, address: String, data: String) {
        val increasedGasLimit = (gasLimit.toDouble()*1.25).toBigDecimal().toBigInteger()
        getLogger()
            .info("Attempt of swap, gasPrice: $gasPrice gasLimit: $gasLimit value: $value address: $address")
        val tx = manager.sendTransaction(BigInteger.valueOf(20), BigInteger.valueOf(20), address, data, value)
        getLogger().info(tx.result)
        getLogger().info(tx.transactionHash)
    }
}
