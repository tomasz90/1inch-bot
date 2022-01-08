package com.oneinch.api.gas_station

import com.oneinch.util.logErrorMessage
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
class GasStationClient(val gasStation: GasStationApi) {

    fun getPrice(): Double? {
        val response = gasStation.getPrice().execute()
        return if (response.isSuccessful) {
            response.body()?.fastest
        } else {
            response.logErrorMessage("Error getting prices.")
            null
        }
    }
}