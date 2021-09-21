import com.esaulpaugh.headlong.abi.Function
import com.github.openjson.JSONObject
import com.oneinch.config.AbiLoader
import com.oneinch.util.SlippageModifier
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

@TestConfiguration
@Import(AbiLoader.class)
abstract class BaseTest {

    @Autowired
    AbiLoader abiLoader

    @Bean
    Function function() {
        JSONObject json = abiLoader.load()
        return Function.fromJson(json.toString())
    }

    @Bean
    SlippageModifier slippageModifier() {
        return new SlippageModifier(function())
    }
}
