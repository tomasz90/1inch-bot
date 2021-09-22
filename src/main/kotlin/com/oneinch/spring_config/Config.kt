package com.oneinch.spring_config

import com.oneinch.`object`.Chain
import com.oneinch.config.PropertiesLoader
import com.oneinch.config.ProtocolsLoader
import com.oneinch.config.SettingsLoader
import com.oneinch.api.one_inch.api.ApiProvider
import com.oneinch.util.RateLimiter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.reflect.full.declaredMemberProperties

@Configuration
open class Config {

    @Autowired
    lateinit var propertiesLoader: PropertiesLoader

    @Autowired
    lateinit var settingsLoader: SettingsLoader

    @Autowired
    lateinit var protocolsLoader: ProtocolsLoader

    @Bean
    open fun properties() = propertiesLoader.load()

    @Bean
    open fun settings() = settingsLoader.load()

    @Bean
    open fun allProtocols() = protocolsLoader.load()

    @Bean
    open fun chain(): Chain {
        val instance = properties()
        return instance.javaClass.kotlin
            .declaredMemberProperties
            .filter { it.name == settings().chain }
            .map { it.get(instance) }
            .first() as Chain
    }

    @Bean
    open fun protocols(): String {
        val instance = allProtocols()
        val protocols = instance.javaClass.kotlin
            .declaredMemberProperties
            .filter { it.name == settings().chain }
            .map { it.get(instance) }
            .first() as List<String>
        return instance.asString(protocols)
    }

    @Bean
    open fun oneInch() = ApiProvider(properties(), settings()).createOneInch()

    @Bean
    open fun isSwapping() = AtomicBoolean()

    @Bean
    open fun setNotSwapping() {
        isSwapping().set(false) // set for startup only
    }

    @Bean
    open fun limiter() = RateLimiter(settings().maxRps)
}