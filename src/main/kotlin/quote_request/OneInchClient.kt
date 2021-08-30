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
import java.math.BigInteger

class OneInchClient {

    fun swap(chainId: Int, from: String, to: String, amount: Amount) {
        val response = ONE_INCH_API.swap(chainId, from, to, amount.origin!!, MY_ADDRESS, MAX_SLIPPAGE).execute()
        if (response.isSuccessful) {
            val body = response.body()!!
            performTxIfGoodRate(body)
        } else {
            response.logErrorMessage("Error during swap, response status: ${response.code()}")
        }
    }

    private fun performTxIfGoodRate(response: SwapResponse) {
        val gasPrice = response.tx.gasPrice
        if(gasPrice > BigInteger.valueOf(100000000000)) {
            getLogger().info("GAS price to high, price: $gasPrice")
            return
        }
        val fromAmount = Amount(response.fromTokenAmount, response.fromToken.decimals)
        val toAmount = Amount(response.toTokenAmount, response.toToken.decimals)
        val percent = calculateAdvantage(fromAmount, toAmount)
        logRatesInfo(response, percent, fromAmount, toAmount)
        if (percent > DEMAND_PERCENT_ADVANTAGE) {
            val tx = response.tx
            getLogger().info(
                "SWAP: fromToken ${response.fromToken.symbol}, toToken: ${response.toToken.symbol}, " +
                        "fromAmount: ${fromAmount.readable}, toAmount: ${toAmount.readable}"
            )
            Sender.sendTransaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data)
        }
    }
}

fun <T> Response<T>.logErrorMessage(info: String) {
    val msg = JSONObject(this.errorBody()!!.charStream().readText()).getString("message")
    getLogger().error("$info\n $msg")
}


