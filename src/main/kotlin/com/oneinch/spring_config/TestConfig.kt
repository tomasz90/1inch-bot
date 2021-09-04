package com.oneinch.spring_config

import com.oneinch.config.Settings
import com.oneinch.on_chain_api.balance.FakeBalance
import com.oneinch.on_chain_api.sender.FakeSender
import com.oneinch.one_inch_api.requester.FakeRequester
import com.oneinch.repository.FakeRepositoryManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("fakeAccount")
open class TestConfig {

    @Autowired
    lateinit var settings: Settings

    @Autowired
    lateinit var repository: FakeRepositoryManager

    @Bean
    open fun myAddress() = settings.myAddress

    @Bean
    open fun balance() = FakeBalance(repository)

    @Bean
    open fun sender() = FakeSender(repository)

    @Bean
    open fun requester() = FakeRequester(sender())

}