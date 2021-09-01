package com.oneinch.oneinch_api

import com.oneinch.InputConfig.MAX_SLIPPAGE
import com.oneinch.on_chain_api.Sender
import com.oneinch.oneinch_api.api.ApiConfig.ONE_INCH_API
import com.oneinch.oneinch_api.api.QuoteException
import com.oneinch.oneinch_api.api.SwapException
import com.oneinch.oneinch_api.api.data.*
import getLogger
import org.json.JSONObject
import org.springframework.stereotype.Component
import retrofit2.Response

@Component
class OneInchClient(private val myAddress: String, private val sender: Sender) {

    fun quote(chainId: Int, from: TokenQuote, to: Token): QuoteDto {
        val response = ONE_INCH_API.quote(chainId, from.token.address, to.address, from.origin).execute()
        if (response.isSuccessful) {
            return response.body()!!.toDto()
        } else {
            throw QuoteException(response)
        }
    }

    fun swap(chainId: Int, from: TokenQuote, to: Token): SwapDto {
        val response =
            ONE_INCH_API.swap(chainId, from.token.address, to.address, from.origin, myAddress, MAX_SLIPPAGE).execute()
        if (response.isSuccessful) {
            return response.body()!!.toDto()
        } else {
            throw SwapException(response)
        }
    }
}


