package quote_request

import BASE_URL
import addDecimals
import calculateAdvantage
import checkOpportunity
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import getLogger
import logRatesInfo
import okhttp3.OkHttpClient
import removeDecimals
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface OneInchService {

    @GET("v3.0/{id}/quote")
    fun quote(
        @Path("id") chainId: Int,
        @Query("fromTokenAddress") from: String,
        @Query("toTokenAddress") to: String,
        @Query("amount") amount: String
    ): Call<QuoteResponse>

    @GET("v3.0/{id}/approve/calldata")
    fun approve(
        @Path("id") chainId: Int,
        @Query("tokenAddress") tokenAddress: String,
        @Query("infinity") infinity: String? = null,
        @Query("amount") amount: String? = null
    ): Call<ApprovalResponse>

    @GET("v3.0/{id}/swap")
    fun swap(
        @Path("id") chainId: Int,
        @Query("fromTokenAddress") fromTokenAddress: String,
        @Query("toTokenAddress") toTokenAddress: String,
        @Query("amount") amount: String,
        @Query("fromAddress") fromAddress: String,
        @Query("slippage") slippage: String,
        @Query("gasPrice") gasPrice: String? = null,
        @Query("allowPartialFill") allowPartialFill: String? = null
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

    fun getQuote(chainId: Int, from: Token, to: Token, fromQuote: String) {
        val response =
            oneInchService.quote(chainId, from.address, to.address, fromQuote.addDecimals(from.decimals)).execute()
        if (response.isSuccessful) {
            val toQuote = response.body()?.amountReceived?.removeDecimals(to.decimals).toString()
            val percent = calculateAdvantage(fromQuote, toQuote)
            val isOpportunity = checkOpportunity(percent)
            logRatesInfo(from, to, fromQuote, toQuote, percent, isOpportunity)
        } else {
            getLogger().info("Error, response status: ${response.code()}")
        }
    }
}

class Token(val name: String, val address: String, val decimals: Int)


