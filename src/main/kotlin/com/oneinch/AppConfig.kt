package com.oneinch

import com.oneinch.InputConfig.MY_ADDRESS
import com.oneinch.on_chain_tx.WalletManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager

@Configuration
open class AppConfig {

    @Bean
    open fun web3service() = HttpService(InputConfig.CHAIN.rpc)

    @Bean
    open fun web3j() = JsonRpc2_0Web3j(web3service())

    @Bean
    open fun credentials() = WalletManager().openWallet()

    @Bean
    open fun rawTransactionManager() = RawTransactionManager(web3j(), credentials(), InputConfig.CHAIN.id.toLong())

    @Bean
    open fun myAddress() = MY_ADDRESS

}