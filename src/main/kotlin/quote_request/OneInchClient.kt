package quote_request

import Amount
import Config.DEMAND_PERCENT_ADVANTAGE
import Config.MAX_SLIPPAGE
import Config.MY_ADDRESS
import Config.ONE_INCH_API
import calculateAdvantage
import getLogger
import logRatesInfo
import on_chain_tx.Sender
import org.json.JSONObject
import retrofit2.Response

class OneInchClient {

    fun getQuote(chainId: Int, from: String, to: String, amount: Amount) {
        val response = ONE_INCH_API.quote(chainId, from, to, amount.origin!!).execute()
        if (response.isSuccessful) {
            val body = response.body()!!
            performTxIfGoodRate(chainId, body)
        } else {
            response.logErrorMessage("Error during quote, response status: ${response.code()}")
        }
    }

    fun swap(chainId: Int, from: String, to: String, amount: Amount) {
        val response = ONE_INCH_API.swap(chainId, from, to, amount.origin!!, MY_ADDRESS, MAX_SLIPPAGE).execute()
        if (response.isSuccessful) {
            val body = response.body()!!
            val tx = body.tx
            getLogger().info(
                "SWAP: fromToken ${body.fromToken}, toToken: ${body.toToken}, " +
                        "fromAmount: ${body.fromTokenAmount}, toAmount: ${body.toTokenAmount}"
            )
            Sender.sendTransaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data)
        } else {
            response.logErrorMessage("Error during swap, response status: ${response.code()}")
        }
    }

    private fun performTxIfGoodRate(chainId: Int, response: QuoteResponse) {
        val fromAmount = Amount(response.fromTokenAmount, response.fromToken.decimals)
        val toAmount = Amount(response.toTokenAmount, response.toToken.decimals)
        val percent = calculateAdvantage(fromAmount, toAmount)
        logRatesInfo(response, percent, fromAmount, toAmount)
        if (percent > DEMAND_PERCENT_ADVANTAGE) {
            getLogger().info("\n\nSwap attempt, advantage: $percent")
            //swap(chainId, response.fromToken.address, response.toToken.address, fromAmount)
        }
    }
}

fun <T> Response<T>.logErrorMessage(info: String) {
    val msg = JSONObject(this.errorBody()!!.charStream().readText()).getString("message")
    getLogger().error("$info\n $msg")
}


