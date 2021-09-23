package com.oneinch.config

import com.oneinch.`object`.Chain

object PropertiesLoader : IResources<Properties> {
    override fun load(): Properties {
        val bufferedReader = readFile("properties.yml")
        return bufferedReader.use { getYmlMapper().readValue(it, Properties::class.java) }
    }
}

class Properties(
    val oneInchUrl: String,
    val gasStationUrl: String,
    val chains: List<Chain>
)