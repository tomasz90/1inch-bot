package com.oneinch.profiles

import com.oneinch.config.Properties
import com.oneinch.config.Settings
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
    lateinit var properties: Properties

    @Autowired
    lateinit var settings: Settings

    @Bean
    open fun myAddress() = settings.myAddress

    @Bean
    open fun oneInch() = ApiProvider(properties).create()

    @Bean
    open fun balance() = FakeBalance()

    @Bean
    open fun sender() = FakeSender()

    @Bean
    open fun requester() = FakeRequester(sender())

}