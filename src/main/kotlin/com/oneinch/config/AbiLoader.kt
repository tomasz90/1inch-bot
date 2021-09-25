package com.oneinch.config

import com.github.openjson.JSONObject
import com.github.openjson.JSONTokener
import com.oneinch.config.FileUtils.readFile
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

@Component
class AbiLoader(val resourceLoader: ResourceLoader) {
    fun load(): JSONObject {
        val bufferedReader = readFile(resourceLoader, "abi.json")
        val tokener = JSONTokener(bufferedReader)
        return JSONObject(tokener)
    }
}