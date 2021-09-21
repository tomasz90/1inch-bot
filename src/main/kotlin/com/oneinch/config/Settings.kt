package com.oneinch.config

import org.springframework.stereotype.Component

@Component
class SettingsLoader : IResources<Settings> {
    override fun load(): Settings {
        val bufferedReader = readFile("settings.yml")
        return bufferedReader.use { getYmlMapper().readValue(it, Settings::class.java) }
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
