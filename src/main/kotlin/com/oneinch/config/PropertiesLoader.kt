package com.oneinch.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.oneinch.common.Chain
import org.springframework.stereotype.Component
import java.nio.file.Files.newBufferedReader
import java.nio.file.Paths

interface IResources<T> {
    fun load(): T
}

@Component
class PropertiesLoader : IResources<PropertiesHolder> {
    override fun load(): PropertiesHolder {
        val dir = Paths.get("src", "main", "resources", "properties.yml")
        val mapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
        return newBufferedReader(dir).use { mapper.readValue(it, PropertiesHolder::class.java) }
    }
}

class PropertiesHolder(val oneInchUrl: String, val bsc: Chain, val matic: Chain)