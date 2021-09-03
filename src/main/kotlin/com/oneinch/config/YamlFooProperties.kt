package com.oneinch.config

import com.oneinch.utils.YamlPropertySourceFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@ConfigurationProperties(prefix = "application")
@PropertySource(value = ["classpath:abc.yml"], factory = YamlPropertySourceFactory::class)
open class YamlFooProperties {

    var users: List<User>? = null

}

class User {
    var name: String? = null
    var age: Int? = null
}