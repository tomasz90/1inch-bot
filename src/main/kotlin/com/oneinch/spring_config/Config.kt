package com.oneinch.spring_config

import com.oneinch.`object`.Chain
import com.oneinch.loader.Properties
import com.oneinch.loader.Protocol
import com.oneinch.loader.Protocols
import com.oneinch.loader.Settings
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class Config {

    @Autowired
    lateinit var properties: Properties

    @Autowired
    lateinit var settings: Settings

    @Autowired
    lateinit var allProtocols: Protocols

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

}