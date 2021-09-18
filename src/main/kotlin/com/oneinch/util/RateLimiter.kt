package com.oneinch.util

import io.github.resilience4j.ratelimiter.RateLimiter
import io.github.resilience4j.ratelimiter.RequestNotPermitted
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

fun <T, S> RateLimiter.decorateSuspendedConsumer(block: suspend (T, S) -> Unit): suspend (T, S) -> Unit {
    return { t: T, s: S -> executeSuspendConsumer(t, s, block) }
}

suspend fun <T, S> RateLimiter.executeSuspendConsumer(t: T, s: S, block: suspend (T, S) -> Unit) {
    awaitPermission()
    block(t, s)
}

internal suspend fun RateLimiter.awaitPermission() {
    val waitTimeNs = reservePermission()
    when {
        waitTimeNs > 0 -> delay(TimeUnit.NANOSECONDS.toMillis(waitTimeNs))
        waitTimeNs < 0 -> throw RequestNotPermitted.createRequestNotPermitted(this)
    }
}

