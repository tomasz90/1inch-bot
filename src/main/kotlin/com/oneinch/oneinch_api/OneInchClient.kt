package com.oneinch.oneinch_api

import com.oneinch.InputConfig.DEMAND_PERCENT_ADVANTAGE
import com.oneinch.InputConfig.MAX_SLIPPAGE
import com.oneinch.on_chain_api.Sender
import com.oneinch.oneinch_api.api.ApiConfig.ONE_INCH_API
import com.oneinch.oneinch_api.api.data.SwapDto
import com.oneinch.oneinch_api.api.data.Token
import com.oneinch.oneinch_api.api.data.TokenQuote
import com.oneinch.oneinch_api.api.data.toDto
import getLogger
import logRatesInfo
import logSwapInfo
import org.json.JSONObject
import org.springframework.stereotype.Component
import retrofit2.Response

@Component
class OneInchClient(private val myAddress: String, private val sender: Sender) {

    fun quote(chainId: Int, from: TokenQuote, to: Token) {
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
            ONE_INCH_API.swap(chainId, from.token.address, to.address, from.origin, myAddress, MAX_SLIPPAGE).execute()
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


