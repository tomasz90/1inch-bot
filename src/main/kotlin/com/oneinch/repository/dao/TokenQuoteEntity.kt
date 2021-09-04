package com.oneinch.repository.dao

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType.TABLE
import javax.persistence.Id

@Entity
class TokenQuoteEntity() {

    @GeneratedValue(strategy = TABLE)
    @Id
    var id: Long? = null
    lateinit var address: String

    constructor(address: String): this() {
        this.address = address
    }
}