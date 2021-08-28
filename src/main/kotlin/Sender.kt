import org.web3j.crypto.Credentials
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Keys
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.Transfer
import org.web3j.utils.Convert.Unit.ETHER
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.system.exitProcess

fun main() {

    val web3service = HttpService("https://polygon-mainnet.infura.io/v3/fac98e56ea7e49608825dfc726fab703")
    val web3 = JsonRpc2_0Web3j(web3service)
    val keys = ECKeyPair.create(
        BigInteger("82596650919171886625369893250123334326338557372459095847543019503875525979057")
    )
    //val keys = Keys.createEcKeyPair()
    val creds = Credentials.create(keys)
    println(creds.address)
    val transactionManager = RawTransactionManager(web3, creds, 137)

    val gasPrice = BigInteger.valueOf(220000000000L)
    val gasLimit = BigInteger.valueOf(50000)
    val value = BigInteger.valueOf(500000000000)
    val hash = transactionManager.sendTransaction(
        gasPrice,
        gasLimit,
        "0x03de2960E4Ee13F480CDf82F8106E77622C35DB0",
        "0x",
        value
    ).transactionHash
    println(hash)
    exitProcess(0)
//    var transactionReceipt = Transfer.sendFunds(
//        web3, creds, "0x03de2960E4Ee13F480CDf82F8106E77622C35DB0",
//        BigDecimal.valueOf(1.0), ETHER
//    ).sendAsync().get()
//    var etherReceipt = transactionReceipt.transactionHash
}
