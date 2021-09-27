package com.oneinch.provider

import com.oneinch.api.telegram.TelegramClient
import com.oneinch.loader.Settings
import com.oneinch.util.getLogger
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.time.Duration.ofHours
import java.time.Instant

@Component
class AdvantageProvider(val settings: Settings, val telegramClient: TelegramClient) {

    var advantage = 0.0
    private var defaultAdvantage = settings.minAdvantage
    private lateinit var deadline: Instant
    private val HALF_HOUR = 1800000L
    private val coroutine = CoroutineScope(CoroutineName("lastTransactionTimer"))

    init {
        resetToDefault()
        coroutine.launch { setToZeroWhenNoTransactionInLastHours() }
    }

    fun resetToDefault() {
        advantage = defaultAdvantage
        deadline = Instant.now() + ofHours(settings.maxTimeNoTransaction)
    }

    private suspend fun setToZeroWhenNoTransactionInLastHours() {
        while (true) {
            delay(HALF_HOUR)
            val now = Instant.now()
            if (now.isAfter(deadline) && advantage == defaultAdvantage) {
                advantage = 0.0
                getLogger().info("No transaction in last ${settings.maxTimeNoTransaction} hours, changed advantage to: $advantage")
                telegramClient.sendNoTransactionsMessage(settings.maxTimeNoTransaction)
            }
        }
    }
}