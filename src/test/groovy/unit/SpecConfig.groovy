package unit

import com.esaulpaugh.headlong.abi.Function
import com.github.openjson.JSONObject
import com.oneinch.loader.AbiLoader
import com.oneinch.loader.Properties
import com.oneinch.loader.PropertiesLoader
import com.oneinch.provider.SlippageProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@TestConfiguration
@Import([AbiLoader.class, PropertiesLoader.class])
abstract class SpecConfig {

    @Autowired
    JSONObject abi

    @Autowired
    PropertiesLoader propertiesLoader

    @Bean
    Properties properties() {
        return propertiesLoader.load()
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
