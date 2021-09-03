package com.oneinch.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Paths

@Component
class SettingsLoader : IResources<Settings> {
    override fun load(): Settings {
        val dir = Paths.get("src", "main", "resources", "settings.yml")
        val mapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
        return Files.newBufferedReader(dir).use { mapper.readValue(it, Settings::class.java) }
    }
}

class Settings(
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