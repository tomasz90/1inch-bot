package com.oneinch.repository

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.on_chain_api.tx.FakeTransaction
import com.oneinch.repository.dao.FakeTokenQuoteEntity
import com.oneinch.repository.dao.toTokenQuote
import org.springframework.stereotype.Component
import java.lang.NullPointerException

@Component
open class FakeRepositoryManager(val repository: IRepository, val chain: Chain) : IRepositoryManager {

    init {
        fillWithFakeBalanceIfEmpty()
    }

    fun save(TokenQuoteEntity: FakeTokenQuoteEntity): FakeTokenQuoteEntity {
        return repository.save(TokenQuoteEntity)
    }

    fun saveTransaction(from: TokenQuote, to: TokenQuote, t: FakeTransaction) {
        TODO("Not yet implemented")
    }

    fun getBalance(erc20: Token): TokenQuote? {
        return repository.findByAddress(erc20.address)?.toTokenQuote()
    }

    fun updateBalance(from: TokenQuote, to: TokenQuote) {
//        val fromQuote = repository.findByAddress(from.address)!!
//        fromQuote.readable = from.readable.toBigDecimal()
//        val toQuote = repository.findByAddress(to.address)
//
//        repository.save(fromQuote)
//        if (toQuote != null) {
//            val to = createTokenQuoteEntity(to.symbol, to.readable)
//            repository.save(to)
//        } else {
//            toQuote?.readable = to.readable.toBigDecimal()
//            repository.save(toQuote)
//        }
    }

    private fun fillWithFakeBalanceIfEmpty() {
        if (repository.count() == 0L) {
            val usdc = createTokenQuoteEntity("USDC", 1000.0)
            val usdt = createTokenQuoteEntity("USDT", 10000.0)
            save(usdc)
            save(usdt)
        }
    }

    private fun createTokenQuoteEntity(symbol: String, readable: Double): FakeTokenQuoteEntity {
        val token = chain.tokens.first { it.symbol == symbol }
        return FakeTokenQuoteEntity(token.symbol, token.address, readable)
    }
}