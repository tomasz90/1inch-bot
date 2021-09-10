package com.oneinch.one_inch_api.requester

import com.oneinch.Utils
import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.config.Settings
import com.oneinch.one_inch_api.OneInchClient
import kotlinx.coroutines.CoroutineScope
import org.springframework.beans.factory.annotation.Autowired

abstract class AbstractRequester {

    @Autowired
    lateinit var oneInchClient: OneInchClient

    @Autowired
    lateinit var settings: Settings

    @Autowired
    lateinit var utils: Utils

    @Autowired
    lateinit var chain: Chain

    open suspend fun swap(from: TokenQuote, to: Token, coroutine: CoroutineScope){}

    fun isRateGood(from: TokenQuote, to: TokenQuote, percentage: Double, demandAdvantage: Double): Boolean {
        utils.logRatesInfo(from, to, percentage, demandAdvantage)
        if (percentage > demandAdvantage) {
            utils.logSwapInfo(from, to)
            return true
        }
        return false
    }
}