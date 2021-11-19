package com.oneinch.api.paraswap.api

import com.oneinch.api.paraswap.api.data.PriceResponse
import com.oneinch.api.paraswap.api.data.TransactionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ParaswapApi {

    @GET("/prices")
    fun price(
        @Query("srcToken") srcToken: String,
        @Query("srcDecimals") srcDecimals: String, // can be omitted if srcToken is a symbol
        @Query("destToken") destToken: String,
        @Query("destDecimals") destDecimals: String,
        @Query("amount") amount: String,
        @Query("side") side: Side,
        @Query("network") network: Int, // chain-id
        @Query("includeDXES") includeDexs: Array<String>,
        @Query("excludeDEXS") excludeDexs: Array<String>,
        @Query("userAddress") userAddress: String,
        @Query("partner") partner: String
    ): Call<PriceResponse>

    @POST("transactions/{network}")
    fun transaction(
        @Path("network") network: Int,
        @Query("ignoreChecks") ignoreChecks: Boolean, // if true response will not give gas param
        @Query("ignoreGasEstimate") ignoreGasEstimate: Boolean, // if true response will not give gas param
        @Query("onlyParams") onlyParams: Boolean, // allows to return only contract params
        @Query("gasPrice") gasPrice: String? = null, // ex. fast
    ): Call<TransactionResponse>
}

enum class Side {
    SELL, BUY
}
