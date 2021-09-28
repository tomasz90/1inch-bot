package com.oneinch.api

import com.oneinch.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
//@Profile("realAccount")
class UpdateController(val mainCoroutine: CoroutineScope) {

    @GetMapping("/updateBalance")
    fun updateBalance(): String {
        //balance.updateAll()
        return "Updating balance..."
    }

    @GetMapping("/restart")
    fun restartApp(): String {
        mainCoroutine.cancel()
        App.restart()
        return "Restarting app..."
    }
}