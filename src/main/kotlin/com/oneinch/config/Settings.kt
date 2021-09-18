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
    val allowPartialFill: Boolean,
    val increasedGasLimit: Double,
    val increasedGasPrice: Double,
    val maxRps: Int,
    val timeout: Long,
    val waitTimeAfterSwap: Long
    )

class SwapSettings(val advantage: Double, val slippage: Double)
