package com.oneinch.config

import org.springframework.stereotype.Component

@Component
class ProtocolsLoader : IResources<Protocols> {
    override fun load(): Protocols {
        val bufferedReader = readFile("protocols.yml")
        return bufferedReader.use { getYmlMapper().readValue(it, Protocols::class.java) }
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
