package com.oneinch.util

import com.oneinch.api.gas_station.GasStationClient
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class GasPriceProvider(val gasStationClient: GasStationClient) {

    val gasPrice: AtomicLong = AtomicLong(10_000_000_000) // default 10 gwei
    private val coroutine = CoroutineScope(CoroutineName("gasPriceProvider"))

    init {
        coroutine.launch { getGasPrice() }
    }

    private suspend fun getGasPrice() {
        while (true) {
            val gweiPrice = gasStationClient.getPrice()
            if (gweiPrice != null) {
                gasPrice.set(gweiPrice.fast.toLong() * 1_000_000_000)
            }
            delay(5000)
        }
    }
}