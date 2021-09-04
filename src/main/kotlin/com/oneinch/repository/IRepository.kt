package com.oneinch.repository

import com.oneinch.repository.dao.FakeTokenQuoteEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
interface IRepository : CrudRepository<FakeTokenQuoteEntity, Long> {

    fun findByAddress(address: String): FakeTokenQuoteEntity?
}