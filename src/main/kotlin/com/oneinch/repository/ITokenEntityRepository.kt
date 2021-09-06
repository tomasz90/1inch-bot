package com.oneinch.repository

import com.oneinch.repository.dao.TokenEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ITokenEntityRepository : CrudRepository<TokenEntity, String> {

    fun findByChainId(chainId: Int): List<TokenEntity>
}