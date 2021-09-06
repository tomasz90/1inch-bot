package com.oneinch.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.oneinch.`object`.Chain
import org.springframework.stereotype.Component
import java.nio.file.Files.newBufferedReader
import java.nio.file.Paths

@Component
class PropertiesLoader : IResources<Properties> {
    override fun load(): Properties {
        val dir = Paths.get("src", "main", "resources", "properties.yml")
        val mapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
        return newBufferedReader(dir).use { mapper.readValue(it, Properties::class.java) }
    }
}

class Properties(val oneInchUrl: String, val bsc: Chain, val matic: Chain, val optimism: Chain)