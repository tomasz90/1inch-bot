package com.oneinch.spring_config

import com.oneinch.`object`.Chain
import com.oneinch.config.PropertiesLoader
import com.oneinch.config.ProtocolsLoader
import com.oneinch.config.SettingsLoader
import com.oneinch.provider.ApiProvider
import com.oneinch.util.RateLimiter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.atomic.AtomicBoolean

@Configuration
open class Config {

    @Bean
    open fun properties() = PropertiesLoader.load()

    @Bean
    open fun settings() = SettingsLoader.load()

    @Bean
    open fun allProtocols() = ProtocolsLoader.load()

    @Bean
    open fun apiProvider() = ApiProvider(properties(), settings())

    @Bean
    open fun chain(): Chain {
        val name = settings().chain
        return properties().chains.first { it.name == name }
    }

    @Bean
    open fun protocols(): String {
        val name = settings().chain
        return allProtocols().protocols.first { it.chain == name }.asString()
    }

    @Bean
    open fun oneInch() = apiProvider().createOneInch()

    @Bean
    open fun isSwapping() = AtomicBoolean()

    @Bean
    open fun setNotSwapping() {
        isSwapping().set(false) // set for startup only
    }

    @Bean
    open fun limiter() = RateLimiter(settings().maxRps)
}