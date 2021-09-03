package com.oneinch.example.dao

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import com.oneinch.example.dao.entity.TokenQuoteEntity

@Repository
interface Repo : CrudRepository<TokenQuoteEntity, Long>