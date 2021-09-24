package com.oneinch.api.gas_station

import com.oneinch.api.one_inch.logErrorMessage
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("realAccount")
class GasStationClient(val gasStation: GasStationApi) {

    fun getPrice(): GasResponse? {
        val response = gasStation.getPrice().execute()
        return if (response.isSuccessful) {
            response.body()
        } else {
            response.logErrorMessage("Error getting prices.")
            null
        }
    }
}