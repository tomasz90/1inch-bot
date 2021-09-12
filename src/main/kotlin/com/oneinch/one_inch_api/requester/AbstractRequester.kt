package com.oneinch.one_inch_api.requester

import com.oneinch.Utils
import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.config.Settings
import com.oneinch.one_inch_api.OneInchClient
import kotlinx.coroutines.CoroutineScope
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.atomic.AtomicBoolean

abstract class AbstractRequester {

    @Autowired
    lateinit var oneInchClient: OneInchClient

    @Autowired
    lateinit var settings: Settings

    @Autowired
    lateinit var utils: Utils

    @Autowired
    lateinit var chain: Chain

    @Autowired
    lateinit var isSwapping: AtomicBoolean

    open suspend fun swap(from: TokenQuote, to: Token) {}

    fun isRateGood(from: TokenQuote, to: TokenQuote, realAdvantage: Double, demandAdvantage: Double): Boolean {
        utils.logRatesInfo(from, to, realAdvantage, demandAdvantage)
        return realAdvantage > demandAdvantage
    }
}