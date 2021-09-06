package com.oneinch.repository

import com.oneinch.repository.dao.RealTxEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IRealTxRepository: CrudRepository<RealTxEntity, Long>