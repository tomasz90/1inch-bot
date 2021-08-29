package quote_request

import BASE_URL
import Config.DEMAND_PERCENT_ADVANTAGE
import Token
import calculateAdvantage
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import expand
import getLogger
import logRatesInfo
import okhttp3.OkHttpClient
import on_chain_tx.Sender
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.math.BigInteger
import java.util.concurrent.TimeUnit

interface OneInchService {

    @GET("v3.0/{id}/quote")
    fun quote(
        @Path("id") chainId: Int,
        @Query("fromTokenAddress") from: String,
        @Query("toTokenAddress") to: String,
        @Query("amount") amount: BigInteger
    ): Call<QuoteResponse>

    @GET("v3.0/{id}/approve/calldata")
    fun approve(
        @Path("id") chainId: Int,
        @Query("tokenAddress") tokenAddress: String,
        @Query("infinity") infinity: Boolean? = null,
        @Query("amount") amount: BigInteger? = null
    ): Call<ApprovalResponse>

    @GET("v3.0/{id}/swap")
    fun swap(
        @Path("id") chainId: Int,
        @Query("fromTokenAddress") fromTokenAddress: String,
        @Query("toTokenAddress") toTokenAddress: String,
        @Query("amount") amount: BigInteger,
        @Query("fromAddress") fromAddress: String,
        @Query("slippage") slippage: Double,
        @Query("gasPrice") gasPrice: BigInteger? = null,
        @Query("allowPartialFill") allowPartialFill: Boolean? = null
    ): Call<ApprovalResponse>


}

class OneInchClient {
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
    private val oneInchService: OneInchService = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(jacksonConverter)
        .build()
        .create(OneInchService::class.java)

    fun getQuote(chainId: Int, from: Token, to: Token, fromQuote: Long) {
        val quote = expand(fromQuote, from.decimals)
        val response = oneInchService.quote(chainId, from.address, to.address, quote).execute()
        if (response.isSuccessful) {
            val body = response.body()!!
            checkOpportunity(body)
        } else {
            getLogger().info("Error, response status: ${response.code()}")
        }
    }
}

fun checkOpportunity(response: QuoteResponse) {
    val percent = calculateAdvantage(response)

    if (percent > DEMAND_PERCENT_ADVANTAGE) {

    }
    logRatesInfo(response, percent)
}


