package com.oneinch.repository.crud

import com.oneinch.repository.dao.FakeTokenQuoteEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IFakeBalanceRepository : CrudRepository<FakeTokenQuoteEntity, String> {

    fun findByAddress(address: String): FakeTokenQuoteEntity?

    fun findByChainId(chainId: Int): List<FakeTokenQuoteEntity>
}