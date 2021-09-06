package com.oneinch.repository

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.on_chain_api.tx.FakeTransaction
import com.oneinch.repository.dao.FakeTokenQuoteEntity
import com.oneinch.repository.dao.toTokenQuote
import org.springframework.stereotype.Component
import kotlin.math.pow

@Component
open class FakeRepositoryManager(val repository: IRepository, val chain: Chain) : IRepositoryManager {

    init {
        fillWithFakeBalanceIfEmpty("USDT", 2000.0)
    }

    fun save(TokenQuoteEntity: FakeTokenQuoteEntity): FakeTokenQuoteEntity {
        return repository.save(TokenQuoteEntity)
    }

    fun saveTransaction(from: TokenQuote, to: TokenQuote, t: FakeTransaction) {
        TODO("Not yet implemented, include date")
    }

    fun getBalance(erc20: Token): TokenQuote? {
        return repository.findByAddress(erc20.address)?.toTokenQuote()
    }

    fun updateBalances(from: TokenQuote, to: TokenQuote) {
        removeBalance(from)
        addBalance(to)
    }

    private fun removeBalance(from: TokenQuote) {
        val entity = findEntity(from)
        entity.readable -= from.calcReadable(chain)
        entity.origin = (entity.origin.toBigInteger() - from.origin.toBigDecimal().toBigInteger()).toString()
        save(entity)
    }

    private fun addBalance(to: TokenQuote) {
        val entity = findEntity(to)
        entity.readable += to.calcReadable(chain)
        entity.origin = (entity.origin.toBigInteger() + to.origin.toBigDecimal().toBigInteger()).toString()
        save(entity)
    }

    private fun findEntity(to: TokenQuote): FakeTokenQuoteEntity {
        return repository.findByAddress(to.address) ?: FakeTokenQuoteEntity(to.symbol, to.address, 0.0, "0")
    }

    private fun fillWithFakeBalanceIfEmpty(symbol: String, readable: Double) {
        if (repository.count() == 0L) {
            val token = chain.tokens.first { it.symbol == symbol }
            val origin = (10.0.pow(token.decimals) * readable).toBigDecimal().toBigInteger().toString()
            val entity = FakeTokenQuoteEntity(symbol, token.address, readable, origin)
            save(entity)
        }
    }
}