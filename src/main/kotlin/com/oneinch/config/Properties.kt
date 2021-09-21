package com.oneinch.config

import com.oneinch.`object`.Chain
import org.springframework.stereotype.Component

@Component
class PropertiesLoader : IResources<Properties> {
    override fun load(): Properties {
        val bufferedReader = readFile("properties.yml")
        return bufferedReader.use { getYmlMapper().readValue(it, Properties::class.java) }
    }
}

class Properties(val oneInchUrl: String, val bsc: Chain, val matic: Chain, val optimism: Chain)