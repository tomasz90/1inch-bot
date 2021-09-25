package com.oneinch.loaders

import com.oneinch.util.FileUtils.getYmlMapper
import com.oneinch.util.FileUtils.readFile
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

@Component
class ProtocolsLoader(val resourceLoader: ResourceLoader) {

    @Bean("allProtocols")
    fun load(): Protocols {
        val bufferedReader = readFile(resourceLoader, "protocols.yml")
        return bufferedReader.use { getYmlMapper().readValue(it, Protocols::class.java) }
    }
}

class Protocols(val protocols: List<Protocol>)

class Protocol(val chain: String, val protocols: List<String>) {
    fun asString(): String {
        return protocols.toString()
            .replace(", ", ",")
            .replace("[", "")
            .replace("]", "")
    }
}
