package com.oneinch.api

import com.oneinch.App
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
//@Profile("realAccount")
class UpdateController {

    @GetMapping("/updateBalance")
    fun updateBalance(): String {
        //balance.updateAll()
        return "Updating balance..."
    }

    @GetMapping("/restart")
    fun restartApp(): String {
        App.restart()
        return "Restarting app..."
    }
}