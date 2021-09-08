package com.oneinch.config

import java.io.BufferedReader
import java.nio.file.Files
import java.nio.file.NoSuchFileException
import java.nio.file.Path

interface IResources<T> {
    fun load(): T

    fun readOneOfSource(dir: Path, altDir: Path): BufferedReader {
        return try {
            Files.newBufferedReader(dir)
        } catch (e: NoSuchFileException) {
            Files.newBufferedReader(altDir)
        }
    }
}