package com.oneinch.config

object ProtocolsLoader : IResources<Protocols> {
    override fun load(): Protocols {
        val bufferedReader = readFile("protocols.yml")
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
