package com.oneinch.spring_config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("fakeAccount")
open class FakeConfig {

    @Bean
    open fun myAddress() = "***REMOVED***"

}