package com.oneinch.util

import kotlinx.coroutines.*
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
class Timer {

    private var max = AtomicInteger(0)
    private var counter = AtomicInteger(0)
    private val coroutine = CoroutineScope(CoroutineName("timer"))

    init {
        coroutine.launch { start() }
    }

    fun addCall() {
        counter.addAndGet(1)
    }

    fun getRps() = max.get()

    private suspend fun start() {
        while (true) {
            delay(2000)
            max = AtomicInteger(counter.get()/2)
            counter.set(0)
        }
    }
}