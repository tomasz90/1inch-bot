package com.oneinch.loader

import com.github.openjson.JSONObject
import com.github.openjson.JSONTokener
import com.oneinch.util.FileUtils.readFile
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

@Component
class AbiLoader(val resourceLoader: ResourceLoader) {

    @Bean("abi")
    fun load(): JSONObject {
        val bufferedReader = readFile(resourceLoader, "abi.json")
        val tokener = JSONTokener(bufferedReader)
        return JSONObject(tokener)
    }
}