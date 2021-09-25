package com.oneinch.config

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.core.io.ResourceLoader
import java.io.BufferedReader
import java.io.InputStreamReader

object FileUtils {

    fun readFile(resourceLoader: ResourceLoader, fileName: String): BufferedReader {
        val inputStream = resourceLoader.getResource("classpath:$fileName").inputStream
        return BufferedReader(InputStreamReader(inputStream))
    }

    fun getYmlMapper(): ObjectMapper {
        return ObjectMapper(YAMLFactory()).registerModule(KotlinModule() as Module)
    }
}