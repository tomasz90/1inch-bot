package quote_request

import Config.DEMAND_PERCENT_ADVANTAGE
import Config.MAX_SLIPPAGE
import Config.MY_ADDRESS
import Config.ONE_INCH_API
import Token
import calculateAdvantage
import getLogger
import logRatesInfo
import logSwapInfo
import org.json.JSONObject
import retrofit2.Response

class OneInchClient {

    fun getQuote(chainId: Int, from: Token, to: Token) {
        val response = ONE_INCH_API.quote(chainId, from.address, to.address, from.origin).execute()
        if (response.isSuccessful) {
            val body = response.body()!!
            performTxIfGoodRate(body)
        } else {
            response.logErrorMessage("Error during quote, response status: ${response.code()}")
        }
    }

    fun swap(chainId: Int, from: Token, to: Token) {
        val response = ONE_INCH_API.swap(chainId, from.address, to.address, from.origin, MY_ADDRESS, MAX_SLIPPAGE).execute()
        if (response.isSuccessful) {
            val body = response.body()!!
            performTxIfGoodRate(body)
        } else {
            response.logErrorMessage("Error during swap, response status: ${response.code()}")
        }
    }

    private fun performTxIfGoodRate(response: SwapQuoteResponse) {
//        val gasPrice = response.tx.gasPrice
//        if(gasPrice > BigInteger.valueOf(100000000000)) {
//            getLogger().info("GAS price to high, price: $gasPrice")
//            return
//        }
        val percent = calculateAdvantage(response)
        logRatesInfo(response, percent)
        if (percent > DEMAND_PERCENT_ADVANTAGE) {
            val tx = response.tx
            logSwapInfo(response)
            //Sender.sendTransaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data)
        }
    }
}

fun <T> Response<T>.logErrorMessage(info: String) {
    val msg = JSONObject(this.errorBody()!!.charStream().readText()).getString("message")
    getLogger().error("$info\n $msg")
}


