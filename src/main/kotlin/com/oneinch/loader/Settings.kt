package com.oneinch.loader

import com.oneinch.util.FileUtils.getYmlMapper
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import java.nio.file.Paths

@Component
object SettingsLoader {

    @Bean("settings")
    fun load(): Settings {
        val fileName = "settings.yml"
        val bufferedReader =
            try {
                Files.newBufferedReader(Paths.get(fileName)) // settings with easy access
            } catch (e: NoSuchFileException) {
                Files.newBufferedReader(getResources().resolve(fileName))
            }
        return bufferedReader.use { getYmlMapper().readValue(it, Settings::class.java) }
    }

    private fun getResources(): Path {
        return Paths.get("src", "main", "resources")
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
    val timeout: Long,
    val maxTimeNoTransaction: Long
)

enum class GasMode { standard, fast, fastest }