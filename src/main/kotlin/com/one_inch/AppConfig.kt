package com.one_inch

import com.one_inch.Config.MY_ADDRESS
import com.one_inch.on_chain_tx.WalletManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager

@Configuration
open class AppConfig {

    @Bean
    open fun web3service() = HttpService(Config.CHAIN.rpc)

    @Bean
    open fun web3j() = JsonRpc2_0Web3j(web3service())

    @Bean
    open fun credentials() = WalletManager().openWallet()

    @Bean
    open fun rawTransactionManager() = RawTransactionManager(web3j(), credentials(), Config.CHAIN.id.toLong())

    @Bean
    open fun myAddress() = MY_ADDRESS

}