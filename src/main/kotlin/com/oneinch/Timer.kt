package com.oneinch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component

@Component
class Timer {

    var max = 0
    private var counter = 0

    init {
        GlobalScope.launch { start() }
    }

    suspend fun start() {
        while (true) {
            delay(1000)
            max = counter
            counter = 0
        }
    }

    fun addCall() {
        counter++
    }
}