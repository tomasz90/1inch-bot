package com.oneinch.config

import com.oneinch.utils.YamlPropertySourceFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import kotlin.properties.Delegates.notNull

@Configuration
@ConfigurationProperties(prefix = "properties")
@PropertySource(value = ["classpath:properties.yml"], factory = YamlPropertySourceFactory::class)
open class YamlFooProperties {

    var one_inch_api: String = ""
    var bsc: Chain = Chain()
    var matic: Chain = Chain()
}

class Chain() {
    var id: Int by notNull()
    lateinit var tokens: List<Token>
    lateinit var rpc: String
}

class Token() {
    lateinit var symbol: String
    lateinit var address: String
    var decimals: Int by notNull()
}


