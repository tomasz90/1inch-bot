import com.esaulpaugh.headlong.abi.Function
import com.github.openjson.JSONObject
import com.oneinch.config.AbiLoader
import com.oneinch.util.SlippageModifier
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
abstract class BaseTest {

    @Bean
    Function function() {
        JSONObject json = new AbiLoader().load()
        return Function.fromJson(json.toString())
    }

    @Bean
    SlippageModifier slippageModifier() {
        return new SlippageModifier(function())
    }
}
