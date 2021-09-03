package com.oneinch.profiles

import com.oneinch.common.Chain
import com.oneinch.config.PropertiesLoader
import com.oneinch.config.SettingsLoader
import com.oneinch.on_chain_api.FakeBalance
import com.oneinch.on_chain_api.FakeSender
import com.oneinch.oneinch_api.FakeRequester
import com.oneinch.oneinch_api.api.ApiProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("fakeAccount")
open class TestConfig {

    // TODO: 03.09.2021 One common configuration for both profiles

    @Autowired
    lateinit var propertiesLoader: PropertiesLoader

    @Autowired
    lateinit var settingsLoader: SettingsLoader

    @Bean
    open fun properties() = propertiesLoader.load()

    @Bean
    open fun settings() = settingsLoader.load()

    // TODO: 03.09.2021 by reflection?
    @Bean
    open fun chain(): Chain {
        return when(settings().chain) {
            "bsc" -> properties().bsc
            "matic" -> properties().matic
            else -> throw Exception("No such chain in configuration")
        }
    }

    @Bean
    open fun myAddress() = settings().myAddress

    @Bean
    open fun oneInch() = ApiProvider(properties()).create()

    @Bean
    open fun balance() = FakeBalance()

    @Bean
    open fun sender() = FakeSender()

    @Bean
    open fun requester() = FakeRequester(sender())

}