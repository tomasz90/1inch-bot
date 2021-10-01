package com.oneinch.requester

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.api.one_inch.OneInchClient
import com.oneinch.api.one_inch.api.data.Dto
import com.oneinch.provider.advantage.IAdvantageProvider
import com.oneinch.util.Utils
import org.springframework.beans.factory.annotation.Autowired

abstract class AbstractRequester {

    @Autowired
    lateinit var oneInchClient: OneInchClient

    @Autowired
    lateinit var utils: Utils

    @Autowired
    lateinit var protocols: String

    @Autowired
    lateinit var advantageProvider: IAdvantageProvider

    open suspend fun swap(from: TokenQuote, to: Token) {}

    fun calculateAdvantage(dto: Dto): Double {
        val realAdvantage = (dto.to.usdValue - dto.from.usdValue) / dto.from.usdValue * 100
        utils.logRatesInfo(dto, realAdvantage)
        return realAdvantage
    }
}