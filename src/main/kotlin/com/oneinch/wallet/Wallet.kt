package com.oneinch.wallet

import org.web3j.crypto.*
import org.web3j.crypto.Bip32ECKeyPair.HARDENED_BIT
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
        val seed = MnemonicUtils.generateSeed(mnemonic,"")
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
//        getLogger().debug("Enter password to keystore:")
//        val password = (System.console()?.readPassword() ?: readLine()).toString()
        return WalletUtils.loadCredentials("abc", files[0])
    }
}