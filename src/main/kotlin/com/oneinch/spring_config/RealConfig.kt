package com.oneinch.spring_config

import com.esaulpaugh.headlong.abi.Function
import com.github.openjson.JSONObject
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
open class RealConfig {

    @Autowired
    lateinit var chain: Chain

    @Autowired
    lateinit var abi: JSONObject

    @Bean
    open fun web3j() = JsonRpc2_0Web3j(HttpService(chain.rpc))

    @Bean
    open fun credentials() = Wallet.open()

    @Bean
    open fun myAddress() = credentials().address

    @Bean
    open fun rawTransactionManager() = RawTransactionManager(web3j(), credentials(), chain.id.toLong())

    @Bean
    open fun function(): Function {
        return Function.fromJson(abi.toString())
    }
}