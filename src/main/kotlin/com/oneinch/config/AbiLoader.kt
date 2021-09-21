package com.oneinch.config

import com.github.openjson.JSONObject
import com.github.openjson.JSONTokener
import org.springframework.stereotype.Component
import java.nio.file.Paths

@Component
class AbiLoader : IResources<JSONObject> {

    override fun load(): JSONObject {
        val dir = Paths.get("src", "main", "resources", "abi.json")
        val altDir = Paths.get("abi.json")
        val bufferedReader = readOneOfSource(dir, altDir)
        val tokener = JSONTokener(bufferedReader)
        return JSONObject(tokener)
    }
}