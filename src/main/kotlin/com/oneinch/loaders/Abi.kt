package com.oneinch.loaders

import com.github.openjson.JSONObject
import com.github.openjson.JSONTokener
import com.oneinch.util.FileUtils.readFile
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

@Component
class Abi(val resourceLoader: ResourceLoader) {
    fun load(): JSONObject {
        val bufferedReader = readFile(resourceLoader, "abi.json")
        val tokener = JSONTokener(bufferedReader)
        return JSONObject(tokener)
    }
}