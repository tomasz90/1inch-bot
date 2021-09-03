package com.oneinch.one_inch_api.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.oneinch.config.Properties
import okhttp3.OkHttpClient
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

@Component
class ApiProvider(private val properties: Properties) {

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

    fun create(): OneInchApi = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(properties.oneInchUrl)
        .addConverterFactory(jacksonConverter)
        .build()
        .create(OneInchApi::class.java)
}