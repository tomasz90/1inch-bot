package com.oneinch.example.dao.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class TokenQuoteEntity() {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long? = null
    var address: String? = null

    constructor(id: Long?, title: String?): this() {
        this.id = id
        this.address = title
    }
}