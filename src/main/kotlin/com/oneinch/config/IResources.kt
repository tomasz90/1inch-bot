package com.oneinch.config

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.BufferedReader
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Path
import java.nio.file.Paths

interface IResources<T> {
    fun load(): T

    fun readFile(fileName: String): BufferedReader {
        return try {
            Files.newBufferedReader(getProjectResources().resolve(fileName))
        } catch (e: NoSuchFileException) {
            if (fileName != "settings.yml") {
                Files.newBufferedReader(getResources().resolve(fileName))
            } else {
                Files.newBufferedReader(Paths.get(fileName)) // settings with easy access
            }
        }
    }

    fun getYmlMapper(): ObjectMapper {
        return ObjectMapper(YAMLFactory()).registerModule(KotlinModule() as Module)
    }

    private fun getResources(): Path {
        return Paths.get("resources")
    }

    private fun getProjectResources(): Path {
        return Paths.get("src", "main", "resources")
    }
}