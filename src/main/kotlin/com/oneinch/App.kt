package com.oneinch

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext

@SpringBootApplication
open class App : CommandLineRunner {

    @Autowired
    lateinit var main: Main

    companion object {
        lateinit var profile: String
        lateinit var context: ConfigurableApplicationContext

        @JvmStatic
        fun restart() {
            val args = context.getBean(ApplicationArguments::class.java)
            val thread = Thread {
                context.close()
                context = SpringApplication(App::class.java).run(*args.sourceArgs)
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
    App.context = SpringApplication(App::class.java).run(*args)
}
