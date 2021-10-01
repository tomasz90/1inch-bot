package com.oneinch.util

import com.oneinch.loader.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
class RateLimiter(val settings: Settings, val isSwapping: Mutex, scope: CoroutineScope) {

    var currentCalls = 0

    private var current429 = AtomicInteger(0)
    private var callsDone = AtomicInteger(0)
    private var maxRps = settings.maxRps
    private val coroutine = CoroutineScope(scope.coroutineContext)

    init {
        coroutine.launch { tick() }
    }

    private suspend fun tick() {
        while (true) {
            delay(1000)
            count429()
            currentCalls = callsDone.getAndSet(0)
            current429 = AtomicInteger(0)
        }
    }

    fun <T, S> decorateFunction(function: suspend (T, S) -> Unit): suspend (T, S) -> Unit {
        return { t: T, s: S -> executeFunction(t, s, function) }
    }

    fun increment429() {
        current429.incrementAndGet()
    }

    private fun count429() {
        if (current429.get() > 5 && !isSwapping.isLocked) {
            coolDown()
        }
    }

    private fun coolDown() {
        coroutine.launch {
            stop()
            lowerLimit()
        }
    }

    private suspend fun stop() {
        isSwapping.lock()
        val minutes = 3L
        getLogger().info("Rps limit reached. Stopping for $minutes minutes.")
        delay(minutes * 1000 * 60)
    }

    private suspend fun lowerLimit() {
        maxRps = settings.loweredRps
        isSwapping.unlock()
        val minutes = settings.loweredRpsTimeMinutes
        getLogger().info("Lowering rate limit for $minutes minutes.")
        delay(minutes * 1000 * 60)
        maxRps = settings.maxRps
    }

    private suspend fun <T, S> executeFunction(t: T, s: S, function: suspend (T, S) -> Unit) {
        while (true) {
            if (callsDone.get() < maxRps) {
                callsDone.incrementAndGet()
                function(t, s)
                break
            } else {
                wait()
            }
        }
    }

    private suspend fun wait() {
        delay(10)
    }
}