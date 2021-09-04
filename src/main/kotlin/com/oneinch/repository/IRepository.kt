package com.oneinch.repository

import com.oneinch.repository.dao.TokenQuoteEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IRepository : CrudRepository<TokenQuoteEntity, Long>