package com.oneinch.provider

import com.oneinch.loaders.Settings
import com.oneinch.util.getLogger
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.time.Duration.ofHours
import java.time.Instant

@Component
class AdvantageProvider(val settings: Settings) {

    var advantage = settings.minAdvantage
    private val defaultAdvantage = settings.minAdvantage
    private val coroutine = CoroutineScope(CoroutineName("lastTransactionTimer"))
    private val HALF_HOUR = 1800000L
    private var lastTransactionTimeStamp = Instant.now()

    init {
        coroutine.launch { setToZeroWhenNoTransactionInLastHours(settings.maxTimeNoTransaction) }
    }

    fun resetToDefault() {
        lastTransactionTimeStamp = Instant.now()
        advantage = defaultAdvantage
    }

    private suspend fun setToZeroWhenNoTransactionInLastHours(hours: Long) {
        while (true) {
            delay(HALF_HOUR)
            val now = Instant.now()
            val maxTimeNoTransaction = lastTransactionTimeStamp.plus(ofHours(hours))
            if (now.isAfter(maxTimeNoTransaction) && advantage == defaultAdvantage) {
                advantage = 0.0
                getLogger().info("No transaction in last $hours hours, changed advantage to: $advantage")
            }
        }
    }
}