package quote_request

import Config.DEMAND_PERCENT_ADVANTAGE
import Config.MAX_SLIPPAGE
import Config.MY_ADDRESS
import Config.ONE_INCH_API
import Token
import calculateAdvantage
import expand
import getLogger
import logRatesInfo
import on_chain_tx.Sender
import org.json.JSONObject
import retrofit2.Response

class OneInchClient {

    fun getQuote(chainId: Int, from: Token, to: Token, fromQuote: Double) {
        val quote = expand(fromQuote, from.decimals)
        val response = ONE_INCH_API.quote(chainId, from.address, to.address, quote).execute()
        if (response.isSuccessful) {
            val body = response.body()!!
            performTxIfGoodRate(chainId, body, from, to, fromQuote)
        } else {
            response.logErrorMessage("Error during quote, response status: ${response.code()}")
        }
    }

    fun swap(chainId: Int, from: Token, to: Token, fromQuote: Double) {
        val quote = expand(fromQuote, from.decimals)
        val response = ONE_INCH_API.swap(chainId, from.address, to.address, quote, MY_ADDRESS, MAX_SLIPPAGE).execute()
        if (response.isSuccessful) {
            val tx = response.body()!!.tx
            Sender.sendTransaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data)
        } else {
            response.logErrorMessage("Error during swap, response status: ${response.code()}")
        }
    }

    private fun performTxIfGoodRate(chainId: Int, response: QuoteResponse, from: Token, to: Token, fromQuote: Double) {
        val percent = calculateAdvantage(response)
        logRatesInfo(response, percent)
        if (percent > DEMAND_PERCENT_ADVANTAGE) {
            swap(chainId, from, to, fromQuote)
        }
    }
}

fun <T> Response<T>.logErrorMessage(info: String) {
    val msg = JSONObject(this.errorBody()!!.charStream().readText()).getString("message")
    getLogger().error("$info\n $msg")
}


