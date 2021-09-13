package com.oneinch.one_inch_api.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.config.Settings
import com.oneinch.one_inch_api.OneInchClient
import com.oneinch.util.Timer
import com.oneinch.util.Utils
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
    lateinit var isSwapping: AtomicBoolean

    @Autowired
    lateinit var timer: Timer

    @Autowired
    lateinit var protocols: String

    open suspend fun swap(from: TokenQuote, to: Token) {}

    fun isRateGood(from: TokenQuote, to: TokenQuote, demandAdvantage: Double): Boolean {
        val realAdvantage = utils.calculateAdvantage(from, to)
        utils.logRatesInfo(from, to, realAdvantage, demandAdvantage)
        return realAdvantage > demandAdvantage
    }
}