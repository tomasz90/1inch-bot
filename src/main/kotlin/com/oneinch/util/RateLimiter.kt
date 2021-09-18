package com.oneinch.util

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicLong

class RateLimiter(private val maxRps: Int) {

    var currentRps = 0L
    private var callsDone: AtomicLong = AtomicLong(0)
    private val coroutine = CoroutineScope(CoroutineName("timer"))

    init {
        coroutine.launch { start() }
    }

    private suspend fun start() {
        while (true) {
            delay(1000)
            currentRps = callsDone.get()
            callsDone.set(0)
        }
    }

    fun <T, S> decorateFunction(function: suspend (T, S) -> Unit): suspend (T, S) -> Unit {
        return { t: T, s: S -> executeFunction(t, s, function) }
    }

    private suspend fun <T, S> executeFunction(t: T, s: S, function: suspend (T, S) -> Unit) {
        while (true) {
            when (callsDone.get() < maxRps) {
                true -> {
                    makeCall(t, s, function)
                    break
                }
                false -> wait()
            }
        }
    }

    private suspend fun <T, S> makeCall(t: T, s: S, block: suspend (T, S) -> Unit) {
        block(t, s)
        callsDone.incrementAndGet()
    }

    private suspend fun wait() {
        delay(10)
    }
}

fun main() {
    val rateLimiter = RateLimiter(2)
    val func = rateLimiter.decorateFunction { s: String, t: String -> println(s + t) }

    runBlocking {
        while (true) {
            yield()
            launch { func.invoke("x", "DDD") }
        }
    }
}