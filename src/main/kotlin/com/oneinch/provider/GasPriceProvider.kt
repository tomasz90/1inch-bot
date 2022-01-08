package com.oneinch.provider

import com.oneinch.api.gas_station.GasStationClient
import com.oneinch.loader.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class GasPriceProvider(val gasStationClient: GasStationClient, val settings: Settings, scope: CoroutineScope) {

    val gasPrice: AtomicLong = AtomicLong(100_000_000_000) // default 100 gwei
    private val gasPriceLimit = settings.gasPriceLimit
    private val coroutine = CoroutineScope(scope.coroutineContext)
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
        return if (this < gasPriceLimit) this else gasPriceLimit
    }

    private fun Double.toWei(): Long {
        return this.toLong() * 1_000_000_000
    }
}