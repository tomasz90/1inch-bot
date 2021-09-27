package unit

import com.esaulpaugh.headlong.abi.Function
import com.github.openjson.JSONObject
import com.oneinch.loader.AbiLoader
import com.oneinch.loader.Properties
import com.oneinch.loader.PropertiesLoader
import com.oneinch.loader.Settings
import com.oneinch.loader.SettingsLoader
import com.oneinch.provider.SlippageProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@TestConfiguration
@Import([AbiLoader.class, PropertiesLoader.class, SettingsLoader.class])
abstract class SpecConfig {

    @Autowired
    JSONObject abi

    @Autowired
    PropertiesLoader propertiesLoader

    @Autowired
    SettingsLoader settingsLoader

    @Bean
    Properties properties() {
        return propertiesLoader.load()
    }

    @Bean
    Settings settings() {
        return settingsLoader.load()
    }

    @Bean
    Function function() {
        return Function.fromJson(abi.toString())
    }

    @Bean
    SlippageProvider slippageProvider() {
        return new SlippageProvider(function())
    }
}
