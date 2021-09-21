package com.oneinch.config

import com.github.openjson.JSONObject
import com.github.openjson.JSONTokener
import org.springframework.stereotype.Component

@Component
class AbiLoader : IResources<JSONObject> {

    override fun load(): JSONObject {
        val bufferedReader = readFile("abi.json")
        val tokener = JSONTokener(bufferedReader)
        return JSONObject(tokener)
    }
}