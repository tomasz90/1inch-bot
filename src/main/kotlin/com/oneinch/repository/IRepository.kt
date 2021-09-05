package com.oneinch.repository

import com.oneinch.repository.dao.FakeTokenQuoteEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IRepository : CrudRepository<FakeTokenQuoteEntity, String> {

    fun findByAddress(address: String): FakeTokenQuoteEntity?
}