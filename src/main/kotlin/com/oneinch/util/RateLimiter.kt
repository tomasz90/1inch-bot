package com.oneinch.util

import com.oneinch.loader.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
class RateLimiter(val settings: Settings, scope: CoroutineScope) {

    var currentCalls = 0

    @Volatile
    private var callsDone = AtomicInteger(0)
    private val coroutine = CoroutineScope(scope.coroutineContext)

    init {
        coroutine.launch { resetCalls() }
    }

    private suspend fun resetCalls() {
        while (true) {
            delay(1000)
            currentCalls = callsDone.getAndSet(0)
        }
    }

    fun <T, S> decorateFunction(function: suspend (T, S) -> Unit): suspend (T, S) -> Unit {
        return { t: T, s: S -> executeFunction(t, s, function) }
    }

    private suspend fun <T, S> executeFunction(t: T, s: S, function: suspend (T, S) -> Unit) {
        while (true) {
            if (callsDone.get() < settings.maxRps) {
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