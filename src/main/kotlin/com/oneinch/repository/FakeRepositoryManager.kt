package com.oneinch.repository

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.on_chain_api.tx.FakeTransaction
import com.oneinch.repository.dao.FakeTokenQuoteEntity
import com.oneinch.repository.dao.toTokenQuote
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
open class FakeRepositoryManager (val repository: IRepository, val chain: Chain): IRepositoryManager {

    init {
        fillWithFakeBalance()
    }

    fun save(TokenQuoteEntity: FakeTokenQuoteEntity): FakeTokenQuoteEntity {
        return repository.save(TokenQuoteEntity)
    }

    @EventListener(ApplicationReadyEvent::class)
    fun fillWithFakeBalance() {
        val usdc = createTokenQuoteEntity("USDC", 1000.0)
        val usdt = createTokenQuoteEntity("USDT", 10000.0)
        save(usdc)
        save(usdt)
    }

    fun saveTransaction(from: TokenQuote, to: TokenQuote, t: FakeTransaction) {
        TODO("Not yet implemented")
    }

    fun getBalance(erc20: Token): TokenQuote {
        return repository.findAll().first { it.symbol == erc20.symbol }.toTokenQuote()
    }

    fun updateBalance(from: TokenQuote, to: TokenQuote) {

    }

    private fun getToken(symbol: String): Token {
        return chain.tokens.first { it.symbol == symbol }
    }

    private fun createTokenQuoteEntity(symbol: String, readable: Double): FakeTokenQuoteEntity {
        val token = getToken(symbol)
        return FakeTokenQuoteEntity(token.symbol, token.address, readable, token.decimals)
    }
}