package com.oneinch.config

import com.oneinch.common.Chain
import com.oneinch.oneinch_api.api.ApiProvider
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
    lateinit var propertiesLoader: PropertiesLoader

    @Autowired
    lateinit var settingsLoader: SettingsLoader

    @Bean
    open fun properties() = propertiesLoader.load()

    @Bean
    open fun settings() = settingsLoader.load()

    @Bean
    open fun chain(): Chain {
        return when(settings().chain) {
            "bsc" -> properties().bsc
            "matic" -> properties().matic
            else -> throw Exception("No such chain in configuration")
        }
    }

    @Bean
    open fun web3service() = HttpService()

    @Bean
    open fun web3j() = JsonRpc2_0Web3j(web3service())

    @Bean
    open fun credentials() = Wallet().open()

    @Bean
    open fun myAddress() = credentials().address

    @Bean
    open fun oneInch() = ApiProvider(properties()).create()

    @Bean
    open fun rawTransactionManager() = RawTransactionManager(web3j(), credentials(), chain().id.toLong())

}