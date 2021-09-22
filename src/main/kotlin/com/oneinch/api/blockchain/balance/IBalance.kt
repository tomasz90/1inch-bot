package com.oneinch.api.blockchain.balance

import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote

interface IBalance {

    fun getERC20(erc20: Token): TokenQuote?

}