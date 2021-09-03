package com.oneinch.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.oneinch.common.Chain
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

interface IResources<T> {
    fun load(file: String): T
}

class PropertiesLoader : IResources<PropertiesHolder> {
    override fun load(file: String): PropertiesHolder {
        val resourceDirectory: Path = Paths.get("src", "main", "resources", file)
        val mapper = ObjectMapper(YAMLFactory())
        mapper.registerModule(KotlinModule())
        return Files.newBufferedReader(resourceDirectory).use { mapper.readValue(it, PropertiesHolder::class.java) }
    }
}

class SettingsLoader : IResources<SettingsHolder> {
    override fun load(file: String): SettingsHolder {
        TODO("Not yet implemented")
    }

}

class SettingsHolder {

}

class PropertiesHolder(val oneInchURL: String, val bsc: Chain, val matic: Chain)

fun main() {
    PropertiesLoader().load("properties.yml")
}