package com.oneinch.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.json.JSONObject
import org.springframework.stereotype.Component
import java.nio.file.Paths

@Component
class AbiLoader: IResources<JSONObject> {

    override fun load(): JSONObject {
        val dir = Paths.get("src", "main", "resources", "abi.json")
        val altDir = Paths.get("abi.json")
        val mapper = ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
        val bufferedReader = readOneOfSource(dir, altDir)
        return bufferedReader.use { mapper.readValue(it, JSONObject::class.java) }
    }
}