package com.oneinch.quote_request.api

import BASE_URL
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    private val mapper: ObjectMapper = ObjectMapper()

    init {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    private val jacksonConverter = JacksonConverterFactory.create(mapper)
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()
    val ONE_INCH_API: OneInchApi = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(jacksonConverter)
        .build()
        .create(OneInchApi::class.java)
}