package on_chain_tx

import getLogger
import org.web3j.crypto.Credentials
import org.web3j.crypto.WalletUtils
import java.io.File
import java.io.FilenameFilter

class WalletManager {

    fun generateWallet(password: String) {
        val file = File(System.getProperty("user.dir"))
        file.createNewFile()
        WalletUtils.generateBip39Wallet(password, file)
    }

    fun openWallet(): Credentials {
        val file = File(System.getProperty("user.dir"))
        val filter = FilenameFilter { _: File?, name: String -> name.startsWith("UTC") && name.endsWith(".json") }
        val files = file.listFiles(filter)!!
        if (files.isEmpty()) {
            throw Exception("There is no keystore..")
        }
        getLogger().debug("Enter password to keystore:")
        val password = (System.console()?.readPassword() ?: readLine()).toString()
        return WalletUtils.loadCredentials(password, files[0])
    }
}