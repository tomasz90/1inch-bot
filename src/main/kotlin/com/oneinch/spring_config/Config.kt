package com.oneinch.spring_config

import com.esaulpaugh.headlong.abi.Function
import com.github.openjson.JSONObject
import com.oneinch.`object`.Chain
import com.oneinch.loader.Properties
import com.oneinch.loader.Protocol
import com.oneinch.loader.Protocols
import com.oneinch.loader.Settings
import com.oneinch.wallet.Wallet
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.sync.Mutex
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager

@Configuration
open class Config {

    @Autowired
    lateinit var properties: Properties

    @Autowired
    lateinit var settings: Settings

    @Autowired
    lateinit var allProtocols: Protocols

    @Autowired
    lateinit var abi: JSONObject

    @Bean
    open fun web3j() = JsonRpc2_0Web3j(HttpService(chain().rpc))

    @Bean
    open fun credentials() = Wallet.open()

    @Bean
    open fun myAddress() = credentials().address

    @Bean
    open fun rawTransactionManager() = RawTransactionManager(web3j(), credentials(), chain().id.toLong())

    @Bean
    open fun function(): Function {
        return Function.fromJson(abi.toString())
    }

    @Bean
    open fun chain(): Chain {
        val name = settings.chain
        return properties.chains.first { it.name == name }
    }

    @Bean
    open fun protocols(): String {
        val chain = settings.chain
        val excluded = settings.excludedProtocols
        val filtered = allProtocols.protocols
            .first { protocols -> protocols.chain == chain }
            .protocols.filter { excl -> !excluded.contains(excl) }
        return Protocol(chain, filtered).asString()
    }

    @Bean
    open fun scope() = CoroutineScope(CoroutineName("coroutine"))

    @Bean
    open fun isSwapping() = Mutex()

}