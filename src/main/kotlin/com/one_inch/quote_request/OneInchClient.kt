package com.one_inch.quote_request

import com.one_inch.InputConfig.DEMAND_PERCENT_ADVANTAGE
import com.one_inch.InputConfig.MAX_SLIPPAGE
import com.one_inch.InputConfig.MY_ADDRESS
import getLogger
import logRatesInfo
import logSwapInfo
import com.one_inch.on_chain_tx.Sender
import org.json.JSONObject
import com.one_inch.quote_request.api.ApiConfig.ONE_INCH_API
import com.one_inch.quote_request.api.data.SwapDto
import com.one_inch.quote_request.api.data.Token
import com.one_inch.quote_request.api.data.TokenQuote
import com.one_inch.quote_request.api.data.toDto
import org.springframework.stereotype.Component
import retrofit2.Response

@Component
class OneInchClient(private val sender: Sender) {

    fun getQuote(chainId: Int, from: TokenQuote, to: Token) {
        val response = ONE_INCH_API.quote(chainId, from.token.address, to.address, from.origin).execute()
        if (response.isSuccessful) {
            val dto = response.body()!!.toDto()
            logRatesInfo(dto.from, dto.to, dto.percentage)
        } else {
            response.logErrorMessage("Error during quote.")
        }
    }

    fun swap(chainId: Int, from: TokenQuote, to: Token) {
        val response =
            ONE_INCH_API.swap(chainId, from.token.address, to.address, from.origin, MY_ADDRESS, MAX_SLIPPAGE).execute()
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
            sender.sendTransaction(tx.gasPrice, tx.gas, tx.value, tx.to, tx.data)
        }
    }
}

fun <T> Response<T>.logErrorMessage(info: String) {
    val msg = JSONObject(this.errorBody()!!.charStream().readText()).getString("message")
    getLogger().error("$info Response status: ${this.code()}\n $msg")
}


