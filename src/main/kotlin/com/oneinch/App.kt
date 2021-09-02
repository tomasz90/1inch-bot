package com.oneinch

import kotlinx.coroutines.DelicateCoroutinesApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.StandardEnvironment

@SpringBootApplication
open class App : CommandLineRunner {

    @Autowired
    lateinit var main: Main

    @DelicateCoroutinesApi
    override fun run(vararg args: String?) {
        main.run()
    }
}

fun main(args: Array<String>) {
    setActiveProfile(args,"fakeAccount")
    runApplication<App>(*args)
}

fun setActiveProfile(args: Array<String>, profile: String) {
    val environment: ConfigurableEnvironment = StandardEnvironment()
    environment.setActiveProfiles(profile)
    val application = SpringApplication(App::class.java)
    application.setEnvironment(environment)
    application.run(*args)
}