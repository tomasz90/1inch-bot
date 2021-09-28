package com.oneinch

import com.oneinch.loader.SettingsLoader
import com.oneinch.wallet.Wallet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.StandardEnvironment

@SpringBootApplication
open class App : CommandLineRunner {

    @Autowired
    lateinit var main: Main

    companion object {
        var context: ConfigurableApplicationContext? = null

        fun restart() {
            val args = context?.getBean(ApplicationArguments::class.java)
            val thread = Thread {
                context?.close()
                context = SpringApplication.run(App::class.java, *args?.sourceArgs)
            }
            thread.isDaemon = false
            thread.start()
        }
    }

    override fun run(vararg args: String?) {
        main.run()
    }
}

fun main(args: Array<String>) {
    setActiveProfile(args)
    App.context = runApplication<App>(*args)
}

private fun setActiveProfile(args: Array<String>) {
    val profile = SettingsLoader.load().account
    if (profile == "realAccount") {
        Wallet.assignPassword()
    }
    val environment: ConfigurableEnvironment = StandardEnvironment()
    environment.setActiveProfiles(profile)
    val application = SpringApplication(App::class.java)
    application.setEnvironment(environment)
    application.run(*args)
}