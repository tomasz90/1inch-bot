package com.oneinch.repository.dao

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class TokenEntity(var symbol: String, @Id var address: String, var chainId: Int) {

    constructor(): this("", "", 0)
}