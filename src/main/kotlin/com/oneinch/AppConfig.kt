package com.oneinch

import com.oneinch.wallet.Wallet
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager

@Configuration
@Profile("realAccount")
open class AppConfig {

    @Bean
    open fun web3service() = HttpService(InputConfig.CHAIN.rpc)

    @Bean
    open fun web3j() = JsonRpc2_0Web3j(web3service())

    @Bean
    open fun credentials() = Wallet().open()

    @Bean
    open fun myAddress() = credentials().address

    @Bean
    open fun rawTransactionManager() = RawTransactionManager(web3j(), credentials(), InputConfig.CHAIN.id.toLong())

}