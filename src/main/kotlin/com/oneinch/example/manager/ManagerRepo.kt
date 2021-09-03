package com.oneinch.example.manager

import com.oneinch.example.dao.Repo
import com.oneinch.example.dao.entity.TokenQuoteEntity
import org.springframework.stereotype.Service
import java.util.*

@Service
class ManagerRepo (private val repo: Repo) {
    fun findById(id: Long): Optional<TokenQuoteEntity?> {
        return repo.findById(id)
    }

    fun findAll(): Iterable<TokenQuoteEntity?> {
        return repo.findAll()
    }

    fun save(TokenQuoteEntity: TokenQuoteEntity): TokenQuoteEntity {
        return repo.save(TokenQuoteEntity)
    }

    fun deleteById(id: Long) {
        repo.deleteById(id)
    }

    fun fillDB() {
        save(TokenQuoteEntity(1L, "0x89523895u"))
        save(TokenQuoteEntity(2L, "0x358235923"))
    }
}