package com.oneinch.spring_config

import com.oneinch.config.Settings
import com.oneinch.api.blockchain.balance.FakeBalance
import com.oneinch.api.blockchain.sender.FakeSender
import com.oneinch.requester.FakeRequester
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
    open fun myAddress() = "***REMOVED***"

    @Bean
    open fun balance() = FakeBalance(repository)

    @Bean
    open fun sender() = FakeSender(repository)

    @Bean
    open fun requester() = FakeRequester(sender())

}