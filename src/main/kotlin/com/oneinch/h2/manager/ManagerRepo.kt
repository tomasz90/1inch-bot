package com.oneinch.h2.manager

import com.oneinch.h2.dao.Repo
import com.oneinch.h2.dao.entity.TokenQuoteEntity
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
        save(TokenQuoteEntity("0x113435111"))
        save(TokenQuoteEntity("0x358235923"))
    }
}