package com.oneinch.loader

import com.oneinch.util.FileUtils.getYmlMapper
import com.oneinch.util.FileUtils.readFile
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Paths

@Component
class ProtocolsLoader(val resourceLoader: ResourceLoader) {

    @Bean("allProtocols")
    fun load(): Protocols {
        val fileName = "protocols.yml"
        val bufferedReader =
            try {
                Files.newBufferedReader(Paths.get(fileName)) // protocols with easy access
            } catch (e: NoSuchFileException) {
                readFile(resourceLoader, "protocols.yml")
            }
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
