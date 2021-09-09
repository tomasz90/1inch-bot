package com.oneinch.wallet

import com.oneinch.getLogger
import org.web3j.crypto.Bip32ECKeyPair
import org.web3j.crypto.Bip32ECKeyPair.HARDENED_BIT
import org.web3j.crypto.Credentials
import org.web3j.crypto.MnemonicUtils
import org.web3j.crypto.WalletUtils
import java.io.File
import java.io.FilenameFilter
import java.security.SecureRandom

class Wallet {

    private val file = File(System.getProperty("user.dir"))

    fun generateMnemonic(): String {
        val initialEntropy = ByteArray(16)
        SecureRandom().nextBytes(initialEntropy)
        return MnemonicUtils.generateMnemonic(initialEntropy)
    }

    fun generateFromMnemonic(mnemonic: String, password: String) {
        val path = intArrayOf(44 or HARDENED_BIT, 60 or HARDENED_BIT, 0 or HARDENED_BIT, 0, 0)
        val seed = MnemonicUtils.generateSeed(mnemonic, "")
        val masterKeyPair = Bip32ECKeyPair.generateKeyPair(seed)
        val bip44Keypair = Bip32ECKeyPair.deriveKeyPair(masterKeyPair, path)
        WalletUtils.generateWalletFile(password, bip44Keypair, file, true)
    }

    fun open(): Credentials {
        val filter = FilenameFilter { _: File?, name: String -> name.startsWith("UTC") && name.endsWith(".json") }
        val files = file.listFiles(filter)!!
        if (files.isEmpty()) {
            throw Exception("There is no keystore..")
        }
        val password = "***REMOVED***" //providePassword()
        return WalletUtils.loadCredentials(password, files[0])
    }

    private fun providePassword(): String {
        getLogger().debug("Enter password to keystore:")
        return (System.console()?.readPassword() ?: readLine()).toString()
    }
}