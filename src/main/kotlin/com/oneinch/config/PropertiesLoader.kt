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

@Component
class SettingsLoader : IResources<SettingsHolder> {
    override fun load(): SettingsHolder {
        val dir = Paths.get("src", "main", "resources", "settings.yml")
        val mapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
        return newBufferedReader(dir).use { mapper.readValue(it, SettingsHolder::class.java) }
    }
}

class SettingsHolder(
    val account: String,
    val chain: String,
    val myAddress: String,
    val logDecimalPrecision: Int,
    val minimalSwapQuote: Double,
    val amountToSell: Double,
    val demandPercentAdvantage: Double,
    val maxSlippage: Double,
    val increasedGasLimit: Double
)

class PropertiesHolder(val oneInchUrl: String, val bsc: Chain, val matic: Chain)

fun main() {
    val c = SettingsLoader().load()
    println(c)
}