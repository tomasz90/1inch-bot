package com.oneinch.api

import com.oneinch.App
import com.oneinch.api.blockchain.balance.Balance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UpdateController(val balance: Balance, val scope: CoroutineScope) {

    @GetMapping("/updateBalance")
    fun updateBalance(): String {
        balance.updateAll()
        return "Updating balance..."
    }

    @GetMapping("/restart")
    fun restartApp(): String {
        scope.cancel()
        App.restart()
        return "Restarting app..."
    }

    @GetMapping("/check")
    fun check(): String {
        return "I'm alive"
    }
}