package com.oneinch

import com.oneinch.wallet.FakeWallet
import org.mockito.Mockito.mock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.web3j.tx.RawTransactionManager

@Configuration
@Profile("fakeAccount")
open class TestConfig {

    @Bean
    open fun credentials() = FakeWallet().open() // TODO: 01.09.2021 check how method will be invoked

    @Bean
    open fun myAddress() = ""

    @Bean
    open fun rawTransactionManager() = mock(RawTransactionManager::class.java)
}