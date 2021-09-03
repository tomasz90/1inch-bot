package com.oneinch.h2.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import com.oneinch.h2.dao.entity.TokenQuoteEntity

@Repository
interface Repo : CrudRepository<TokenQuoteEntity, Long>