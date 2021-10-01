package com.oneinch.util

import com.oneinch.loader.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

@Component
class RateLimiter(val settings: Settings, val isSwapping: Mutex, scope: CoroutineScope) {

    var currentCalls = 0

    private var rateLimitReached = AtomicBoolean()
    private var currentLimitations = AtomicInteger(settings.maxRps)
    private var callsDone = AtomicInteger(0)
    private var rps = settings.maxRps
    private val coroutine = CoroutineScope(scope.coroutineContext)

    init {
        coroutine.launch { resetCalls() }
        coroutine.launch { setOptimalCallsRate() }
        coroutine.launch { trackCurrentLimitations() }
    }

    fun <T, S> decorateFunction(function: suspend (T, S) -> Unit): suspend (T, S) -> Unit {
        return { t: T, s: S -> executeFunction(t, s, function) }
    }

    fun notifyRateLimitReached() {
        rateLimitReached.set(true)
    }

    private suspend fun resetCalls() {
        while (true) {
            delay(1_000)
            currentCalls = callsDone.getAndSet(0)
        }
    }

    private suspend fun setOptimalCallsRate() {
        while (true) {
            delay(10_000)
            if (rateLimitReached.get()) {
                stopSwapping()
                decreaseRps()
                resumeSwapping()
            } else {
                increaseRps()
            }
            rateLimitReached.set(false)
        }
    }

    private suspend fun trackCurrentLimitations() {
        while (true) {
            delay(3600_000)
            currentLimitations.set(settings.maxRps)
        }
    }

    private suspend fun stopSwapping() {
        getLogger().info("Stop swapping, cooling down...")
        isSwapping.lock()
        delay(60_000)
    }

    private fun decreaseRps() {
        if (rps > 2) {
            rps -= 2
            currentLimitations.set(rps)
            getLogger().info("Decreasing rps... Setting limitations to: $rps")
        }
    }

    private fun increaseRps() {
        if (rps < currentLimitations.get()) {
            rps += 1
            getLogger().info("Increasing rps...")
        }
    }

    private fun resumeSwapping() {
        isSwapping.unlock()
    }

    private suspend fun <T, S> executeFunction(t: T, s: S, function: suspend (T, S) -> Unit) {
        while (true) {
            if (callsDone.get() < rps) {
                callsDone.incrementAndGet()
                function(t, s)
                break
            } else {
                delay(10)
            }
        }
    }
}