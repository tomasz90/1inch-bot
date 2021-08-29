package on_chain_tx

import MATIC_RPC_URL
import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import java.math.BigInteger
import kotlin.system.exitProcess

fun main() {

    val web3service = HttpService(MATIC_RPC_URL)
    val web3 = JsonRpc2_0Web3j(web3service)

    val keys =
        ECKeyPair.create(BigInteger("73879472168346952077578437103592564545953740477884952342696959757500830031267"))
    // my address:  0x8cf1839B4e5eE89a31d3390eF5a0927918A5Da9A
    println(keys.privateKey)
    //val keys = Keys.createEcKeyPair()
    val creds = Credentials.create(keys)
    println(creds.address)
    val transactionManager = RawTransactionManager(web3, creds, 137)

    val gasPrice = BigInteger.valueOf(22000000000L)
    val gasLimit = BigInteger.valueOf(40000)
    val value = BigInteger.valueOf(500000000)
    val address = "0x03de2960E4Ee13F480CDf82F8106E77622C35DB0"
    val data = "0x";

    val hash = transactionManager.sendTransaction(gasPrice, gasLimit, address, data, value).transactionHash
    println(hash)
    exitProcess(0)
}
