package com.oneinch.repository

import com.oneinch.on_chain_api.tx.FakeTransaction
import com.oneinch.on_chain_api.tx.Transaction
import com.oneinch.one_inch_api.api.data.Token
import com.oneinch.one_inch_api.api.data.TokenQuote
import com.oneinch.repository.dao.TokenQuoteEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class Repository (private val repository: IRepository) {
    fun findById(id: Long): Optional<TokenQuoteEntity?> {
        return repository.findById(id)
    }

    fun findAll(): Iterable<TokenQuoteEntity?> {
        return repository.findAll()
    }

    fun save(TokenQuoteEntity: TokenQuoteEntity): TokenQuoteEntity {
        return repository.save(TokenQuoteEntity)
    }

    fun deleteById(id: Long) {
        repository.deleteById(id)
    }

    fun fillDB() {
        save(TokenQuoteEntity("0x113435111"))
        save(TokenQuoteEntity("0x358235923"))
    }

    fun saveTransaction(from: TokenQuote, to: TokenQuote, t: Transaction, txHash: String) {
        TODO("Not yet implemented")
    }

    fun saveTransaction(from: TokenQuote, to: TokenQuote, t: FakeTransaction) {
        TODO("Not yet implemented")
    }

    fun getBalance(erc20: Token): TokenQuote {
        TODO("Not yet implemented")
    }

    fun updateBalance(from: TokenQuote, to: TokenQuote) {

    }
}