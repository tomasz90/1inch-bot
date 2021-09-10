package com.oneinch.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Component
import java.nio.file.Paths

@Component
class SettingsLoader : IResources<Settings> {
    override fun load(): Settings {
        val dir = Paths.get("src", "main", "resources", "settings.yml")
        val altDir = Paths.get("settings.yml")
        val mapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
        val bufferedReader = readOneOfSource(dir, altDir)
        return bufferedReader.use { mapper.readValue(it, Settings::class.java) }
    }
}

class Settings(
    val account: String,
    val chain: String,
    val myAddress: String,
    val logDecimalPrecision: Int,
    val minimalSwapQuote: Double,
    val swapSettings: List<SwapSettings>,
    val increasedGasLimit: Double,
    val increasedGasPrice: Double,
    val sleepTime: Long,
    val timeout: Long
)

class SwapSettings(val demandAdvantage: Double, val maxSlippage: Double)