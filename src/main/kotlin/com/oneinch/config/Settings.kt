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
    val minSwapQuote: Double,
    val defaultSlippage: Double,
    val minAdvantage: Double,
    val allowPartialFill: Boolean,
    val increasedGasLimit: Double,
    val gasPriceMode: GasMode,
    val gasPriceLimit: Double,
    val maxRps: Int,
    val timeout: Long
)

enum class GasMode { standard, fast, fastest }
