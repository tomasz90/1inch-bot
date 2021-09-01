package com.oneinch

import com.oneinch.InputConfig.MY_ADDRESS
import com.oneinch.wallet.FakeWallet
import org.mockito.Mockito.mock
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.web3j.protocol.core.JsonRpc2_0Web3j
import org.web3j.protocol.http.HttpService
import org.web3j.tx.RawTransactionManager

@Configuration
@Profile("fakeAccount")
open class TestConfig {

    @Bean
    open fun web3service(): HttpService = mock(HttpService::class.java)

    @Bean
    open fun web3j() = mock(JsonRpc2_0Web3j::class.java)

    @Bean
    open fun credentials() = FakeWallet().open() // TODO: 01.09.2021 check how method will be invoked

    @Bean
    open fun rawTransactionManager() = mock(RawTransactionManager::class.java)

    @Bean
    open fun myAddress() = MY_ADDRESS
}