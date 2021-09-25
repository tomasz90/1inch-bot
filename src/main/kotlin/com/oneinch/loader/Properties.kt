package com.oneinch.loader

import com.oneinch.`object`.Chain
import com.oneinch.util.FileUtils.getYmlMapper
import com.oneinch.util.FileUtils.readFile
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

@Component
class PropertiesLoader(val resourceLoader: ResourceLoader) {

    @Bean("properties")
     fun load(): Properties {
        val bufferedReader = readFile(resourceLoader, "properties.yml")
        return bufferedReader.use { getYmlMapper().readValue(it, Properties::class.java) }
    }
}

class Properties(
    val oneInchUrl: String,
    val gasStationUrl: String,
    val chains: List<Chain>
)