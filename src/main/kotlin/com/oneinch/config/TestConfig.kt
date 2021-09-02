package com.oneinch.config

import com.oneinch.config.InputConfig.MY_ADDRESS
import com.oneinch.on_chain_api.FakeBalance
import com.oneinch.on_chain_api.FakeSender
import com.oneinch.oneinch_api.FakeRequester
import com.oneinch.oneinch_api.api.ApiProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("fakeAccount")
open class TestConfig {

    @Bean
    open fun myAddress() = MY_ADDRESS

    @Bean
    open fun oneInch() = ApiProvider().oneInch

    @Bean
    open fun balance() = FakeBalance()

    @Bean
    open fun sender() = FakeSender()

    @Bean
    open fun requester() = FakeRequester(sender())

}