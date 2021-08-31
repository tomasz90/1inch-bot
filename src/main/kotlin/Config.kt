import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import quote_request.OneInchApi
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

object Config {

    val CHAIN = BSC
    const val MY_ADDRESS = "0x0fad488c45e44B72A17e4eBFc20ce16ff284de3E"
    const val AMOUNT_TO_SELL: Double = 20000.0
    const val DEMAND_PERCENT_ADVANTAGE: Double = 1.0
    const val MAX_SLIPPAGE: Double = 0.1
    const val LOG_DECIMAL_PRECISION = 0

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