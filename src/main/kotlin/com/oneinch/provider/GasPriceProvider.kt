package com.oneinch.provider

import com.oneinch.api.gas_station.GasStationClient
import com.oneinch.loaders.GasMode.fast
import com.oneinch.loaders.GasMode.fastest
import com.oneinch.loaders.GasMode.standard
import com.oneinch.loaders.Settings
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
    private val coroutine = CoroutineScope(CoroutineName("gasPriceProvider"))

    init {
        coroutine.launch { getGasPrice() }
    }

    private suspend fun getGasPrice() {
        while (true) {
            val gweiPrice = gasStationClient.getPrice()
            if (gweiPrice != null) {
                val price = when(settings.gasPriceMode) {
                    fastest -> gweiPrice.fastest
                    fast -> gweiPrice.fast
                    standard -> gweiPrice.standard
                }
                gasPrice.set(price.setLimit().toWei())
            }
            delay(2000)
        }
    }

    private fun Double.setLimit(): Double {
        val limit = settings.gasPriceLimit
        return if(this < limit) this else limit
    }

    private fun Double.toWei(): Long {
        return this.toLong() * 1_000_000_000
    }
}