package com.oneinch.api

import com.oneinch.api.blockchain.balance.Balance
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Profile("realAccount")
class UpdateController(val balance: Balance) {

    @GetMapping("/updateBalance")
    fun updateBalance(): String {
        balance.updateAll()
        return "Updating balance..."
    }
}