package com.oneinch.config

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
            Files.newBufferedReader(getResources().resolve(fileName))
        } catch (e: NoSuchFileException) {
            Files.newBufferedReader(Paths.get(fileName))
        }
    }

    fun getYmlMapper(): ObjectMapper {
        return ObjectMapper(YAMLFactory()).registerModule(KotlinModule())
    }

    private fun getResources(): Path {
        return Paths.get("src", "main", "resources")
    }
}