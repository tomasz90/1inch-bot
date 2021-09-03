package com.oneinch.config

import com.oneinch.common.Chain
import com.oneinch.oneinch_api.api.data.Token
import com.oneinch.utils.YamlPropertySourceFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import java.lang.NullPointerException

@Configuration
@ConfigurationProperties(prefix = "properties")
@PropertySource(value = ["classpath:properties.yml"], factory = YamlPropertySourceFactory::class)
open class YamlFooProperties {

    var one_inch_api: String? = null
    var bsc: ChainProperties? = null
    var matic: ChainProperties? = null
}

class ChainProperties(
    var id: Int? = null,
    var tokens: List<TokenProperties>? = null,
    var rpc: String? = null
) {
    fun toChain(): Chain {
        return try {
            Chain(id!!, tokens!!.map { it.toToken() }, rpc!!)
        } catch (e: NullPointerException) {
            throw InvalidFileProperty("Invalid config or mapping objects, check properties in yml files.")
        }
    }
}

class TokenProperties(
    var symbol: String? = null,
    var address: String? = null,
    var decimals: Int? = null
) {
    fun toToken(): Token {
        return try {
            Token(symbol!!, address!!, decimals!!)
        } catch (e: NullPointerException) {
            throw InvalidFileProperty("Invalid config or mapping objects, check properties in yml files.")
        }
    }
}

class InvalidFileProperty(s: String) : Exception(s)

