package com.oneinch.api.gas_station

import retrofit2.Call
import retrofit2.http.GET

interface GasStationApi {

    @GET("/")
    fun getPrice(): Call<GasResponse>
}