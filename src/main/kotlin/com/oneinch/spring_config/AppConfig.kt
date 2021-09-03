package com.oneinch.spring_config

import com.oneinch.`object`.Chain
import com.oneinch.wallet.Wallet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager

@Configuration
@Profile("realAccount")
open class AppConfig {

    @Autowired
    lateinit var chain: Chain

    @Bean
    open fun web3j() = JsonRpc2_0Web3j(HttpService())

    @Bean
    open fun credentials() = Wallet().open()

    @Bean
    open fun myAddress() = credentials().address

    @Bean
    open fun rawTransactionManager() = RawTransactionManager(web3j(), credentials(), chain.id.toLong())

}