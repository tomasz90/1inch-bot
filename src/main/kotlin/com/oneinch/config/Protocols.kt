package com.oneinch.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Component
import java.nio.file.Paths

@Component
class ProtocolsLoader : IResources<Protocols> {
    override fun load(): Protocols {
        val dir = Paths.get("src", "main", "resources", "protocols.yml")
        val altDir = Paths.get("protocols.yml")
        val mapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
        val bufferedReader = readOneOfSource(dir, altDir)
        return bufferedReader.use { mapper.readValue(it, Protocols::class.java) }
    }
}

class Protocols(val matic: List<String>, val bsc: List<String>, val optimism: List<String>) {
    fun asString(protocol: List<String>): String {
        return protocol.toString()
            .replace(", ", ",")
            .replace("[", "")
            .replace("]", "")
    }
}
