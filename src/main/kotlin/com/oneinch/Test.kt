package com.oneinch

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicBoolean


suspend fun main() = coroutineScope {
    val isSwapping = AtomicBoolean()
    isSwapping.set(false)

    while (true) {
        if (!isSwapping.get()) {
            launch { apiCall("1", isSwapping) }
            launch { apiCall("2", isSwapping) }
        }
        yield()
    }
}

suspend fun apiCall(name: String, isSwapping: AtomicBoolean) {
    if (!isSwapping.get()) {
        isSwapping.set(true)
        println("swapping $name")
        delay(4000)
        isSwapping.set(false)
    }
}