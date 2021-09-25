package com.oneinch.repository

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.api.blockchain.tx.FakeTransaction
import com.oneinch.repository.dao.FakeTokenQuoteEntity
import com.oneinch.repository.dao.toTokenQuote
import org.springframework.stereotype.Component

@Component
open class FakeRepositoryManager(val repository: IFakeBalanceRepository, val chain: Chain) {

    init {
        fillWithFakeBalanceIfEmpty("USDC", 10000.0)
    }

    fun save(TokenQuoteEntity: FakeTokenQuoteEntity): FakeTokenQuoteEntity {
        return repository.save(TokenQuoteEntity)
    }

    fun saveTransaction(from: TokenQuote, to: TokenQuote, t: FakeTransaction) {
        TODO("Not yet implemented, include date")
    }

    fun getBalance(erc20: Token): TokenQuote? {
        return repository.findByAddress(erc20.address)?.toTokenQuote(chain)
    }

    fun updateBalances(from: TokenQuote, to: TokenQuote) {
        removeBalance(from)
        addBalance(to)
    }

    private fun removeBalance(from: TokenQuote) {
        val entity = findByAddress(from)
        entity.readable -= from.calcReadable()
        save(entity)
    }

    private fun addBalance(to: TokenQuote) {
        val entity = findByAddress(to)
        entity.readable += to.calcReadable()
        save(entity)
    }

    private fun findByAddress(to: TokenQuote): FakeTokenQuoteEntity {
        return repository.findByAddress(to.token.address) ?: FakeTokenQuoteEntity(chain.id, to.token.symbol, to.token.address, 0.0)
    }

    private fun fillWithFakeBalanceIfEmpty(symbol: String, readable: Double) {
        if (repository.findByChainId(chain.id).isEmpty()) {
            val token = chain.tokens.first { it.symbol == symbol }
            val entity = FakeTokenQuoteEntity(chain.id, symbol, token.address, readable)
            save(entity)
        }
    }
}