package com.oneinch.one_inch_api.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.oneinch.config.Properties
import getLogger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import org.springframework.stereotype.Component
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit


@Component
class ApiProvider(private val properties: Properties) {

    private val mapper: ObjectMapper = ObjectMapper()

    init {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    private val interceptor = TimeoutInterceptorImpl()
    private val jacksonConverter = JacksonConverterFactory.create(mapper)
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
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

interface TimeoutInterceptor: Interceptor

class TimeoutInterceptorImpl : TimeoutInterceptor{

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val errorBody = JSONObject().put("message", "TIMEOUT").toString()
        if (isConnectionTimedOut(chain)) {
            return okhttp3.Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(400)
                .message("client config invalid")
                .body(errorBody.toResponseBody(null))
                .build()
        }
        return chain.proceed(chain.request())
    }

    private fun isConnectionTimedOut(chain: Interceptor.Chain): Boolean {
        try {
            val response = chain.proceed(chain.request())
            response.close()
        } catch (e: SocketTimeoutException) {
            getLogger().info("Response did not return on time - time out")
            return true
        }
        return false
    }
}