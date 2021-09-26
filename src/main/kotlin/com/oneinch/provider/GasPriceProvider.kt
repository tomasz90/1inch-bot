package com.oneinch.provider

import com.oneinch.api.gas_station.GasStationClient
import com.oneinch.loader.Settings
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
@Profile("realAccount")
class GasPriceProvider(val gasStationClient: GasStationClient, val settings: Settings) {

    val gasPrice: AtomicLong = AtomicLong(10_000_000_000) // default 10 gwei
    private val gasPriceLimit = settings.gasPriceLimit
    private val coroutine = CoroutineScope(CoroutineName("gasPriceProvider"))
    private val TWO_SECONDS = 2000L

    init {
        coroutine.launch { setGasPrice() }
    }

    private suspend fun setGasPrice() {
        while (true) {
            val gweiPrice = gasStationClient.getPrice()
            if (gweiPrice != null) {
                val weiPrice = gweiPrice.setLimit().toWei()
                gasPrice.set(weiPrice)
            }
            delay(TWO_SECONDS)
        }
    }

    private fun Double.setLimit(): Double {
        return if(this < gasPriceLimit) this else gasPriceLimit
    }

    private fun Double.toWei(): Long {
        return this.toLong() * 1_000_000_000
    }
}