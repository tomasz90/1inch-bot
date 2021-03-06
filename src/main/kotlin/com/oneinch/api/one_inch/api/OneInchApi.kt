package com.oneinch.api.one_inch.api

import com.oneinch.api.one_inch.api.data.QuoteResponse
import com.oneinch.api.one_inch.api.data.SwapResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigInteger

interface OneInchApi {

    @GET("v3.0/{id}/quote")
    fun quote(
        @Path("id") chainId: Int,
        @Query("fromTokenAddress") from: String,
        @Query("toTokenAddress") to: String,
        @Query("amount") amount: BigInteger
    ): Call<QuoteResponse>

    @GET("v3.0/{id}/swap")
    fun swap(
        @Path("id") chainId: Int,
        @Query("fromTokenAddress") fromTokenAddress: String,
        @Query("toTokenAddress") toTokenAddress: String,
        @Query("amount") amount: BigInteger,
        @Query("fromAddress") fromAddress: String,
        @Query("slippage") slippage: Double,
        @Query("allowPartialFill") allowPartialFill: Boolean,
        @Query("disableEstimate") disableEstimate: Boolean,
        @Query("protocols") protocols: String? = null,
        @Query("complexityLevel") complexityLevel: Int? = null,
        @Query("gasPrice") gasPrice: Long? = null,
        @Query("mainRouteParts") mainRouteParts: Int? = null,
        @Query("parts") parts: Int? = null
    ): Call<SwapResponse>
}