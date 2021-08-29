package on_chain_tx

import Config.CHAIN
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
//        val gasPrice = BigInteger.valueOf(22000000000L)
//        val gasLimit = BigInteger.valueOf(40000)
//        val value = BigInteger.valueOf(500000000)
        val address = "0x03de2960E4Ee13F480CDf82F8106E77622C35DB0"
        val data = "0x"
        manager.sendTransaction(gasPrice, gasLimit, address, data, value).transactionHash
    }
}
