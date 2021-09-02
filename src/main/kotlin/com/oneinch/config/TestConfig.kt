package com.oneinch.config

import com.oneinch.on_chain_api.FakeBalance
import com.oneinch.on_chain_api.FakeSender
import com.oneinch.oneinch_api.FakeRequester
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("fakeAccount")
open class TestConfig {

    @Bean
    open fun myAddress() = ""

    @Bean
    open fun balance() = FakeBalance()

    @Bean
    open fun sender() = FakeSender()

    @Bean
    open fun requester() = FakeRequester(sender())

}