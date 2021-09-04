package com.oneinch.one_inch_api.requester

import com.oneinch.config.Settings
import com.oneinch.one_inch_api.OneInchClient
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import logRatesInfo
import logSwapInfo
import org.springframework.beans.factory.annotation.Autowired

abstract class AbstractRequester {

    @Autowired
    lateinit var oneInchClient: OneInchClient

    @Autowired
    lateinit var settings: Settings

    open fun swap(chainId: Int, from: TokenQuote, to: Token){}

    fun isRateGood(from: TokenQuote, to: TokenQuote, percentage: Double): Boolean {
        logRatesInfo(from, to, percentage)
        if (percentage > settings.demandPercentAdvantage) {
            logSwapInfo(from, to)
            return true
        }
        return false
    }
}