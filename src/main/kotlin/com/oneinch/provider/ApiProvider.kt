package com.oneinch.provider

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.openjson.JSONObject
import com.oneinch.api.gas_station.GasStationApi
import com.oneinch.api.one_inch.api.OneInchApi
import com.oneinch.loader.Properties
import com.oneinch.loader.Settings
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

class ApiProvider(val properties: Properties, val settings: Settings) {

    private val mapper: ObjectMapper = ObjectMapper()

    init {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    private val interceptor = TimeoutInterceptorImpl()
    private val jacksonConverter = JacksonConverterFactory.create(mapper)
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(settings.timeout, TimeUnit.SECONDS)
        .writeTimeout(settings.timeout, TimeUnit.SECONDS)
        .readTimeout(settings.timeout, TimeUnit.SECONDS)
        .build()

    fun createOneInch(): OneInchApi = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(properties.oneInchUrl)
        .addConverterFactory(jacksonConverter)
        .build()
        .create(OneInchApi::class.java)

    fun createGasStation(): GasStationApi = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(properties.gasStationUrl)
        .addConverterFactory(jacksonConverter)
        .build()
        .create(GasStationApi::class.java)
}

interface TimeoutInterceptor : Interceptor

class TimeoutInterceptorImpl : TimeoutInterceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val errorBody = JSONObject().put("message", "TIMEOUT").toString()
        return try {
            chain.proceed(chain.request())
        } catch (e: Exception) {
            okhttp3.Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(400)
                .message("client config invalid")
                .body(errorBody.toResponseBody(null))
                .build()
        }
    }
}