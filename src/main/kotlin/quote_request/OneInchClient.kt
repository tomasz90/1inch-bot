package quote_request

import Config.DEMAND_PERCENT_ADVANTAGE
import Config.MAX_SLIPPAGE
import Config.MY_ADDRESS
import getLogger
import logRatesInfo
import logSwapInfo
import on_chain_tx.Sender
import org.json.JSONObject
import quote_request.api.ApiConfig.ONE_INCH_API
import quote_request.api.data.SwapDto
import quote_request.api.data.SwapResponse
import quote_request.api.data.Token
import quote_request.api.data.toDto
import retrofit2.Response
import kotlin.reflect.typeOf

class OneInchClient {

    fun getQuote(chainId: Int, from: Token, to: Token) {
        val response = ONE_INCH_API.quote(chainId, from.address, to.address, from.origin).execute()
        if (response.isSuccessful) {
            val dto = response.body()!!.toDto()
            logRatesInfo(dto.from, dto.to, dto.percentage)
        } else {
            response.logErrorMessage("Error during quote.")
        }
    }

    fun swap(chainId: Int, from: Token, to: Token) {
        val response =
            ONE_INCH_API.swap(chainId, from.address, to.address, from.origin, MY_ADDRESS, MAX_SLIPPAGE).execute()
        if (response.isSuccessful) {
            val dto = response.body()!!.toDto()
            logRatesInfo(dto.from, dto.to, dto.percentage)
            performTxIfGoodRate(dto)
        } else {
            response.logErrorMessage("Error during swap.")
        }
    }

    private fun performTxIfGoodRate(dto: SwapDto) {
        if (dto.percentage > DEMAND_PERCENT_ADVANTAGE) {
            logSwapInfo(dto.from, dto.to)
            val tx = dto.tx
            Sender().sendTransaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data)
        }
    }
}

fun <T> Response<T>.logErrorMessage(info: String) {
    val msg = JSONObject(this.errorBody()!!.charStream().readText()).getString("message")
    getLogger().error("$info Response status: ${this.code()}\n $msg")
}


