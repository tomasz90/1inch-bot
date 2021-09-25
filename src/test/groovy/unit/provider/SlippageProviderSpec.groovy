package unit.provider

import com.github.openjson.JSONObject
import com.github.openjson.JSONTokener
import com.oneinch.provider.SlippageProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import unit.BaseTest
import unit.TestConfig

import java.nio.file.Files
import java.nio.file.Paths

@ContextConfiguration
@Import(TestConfig.class)
class SlippageProviderSpec extends BaseTest {

    @Autowired
    SlippageProvider slippageProvider

    public static JSONObject txInputData

    static {
        def path = Paths.get("src", "test", "resources", "txInputData.json")
        def bufferedReader = Files.newBufferedReader(path)
        def tokener = new JSONTokener(bufferedReader)
        txInputData = new JSONObject(tokener)
    }

    def "should modify minimum return amount"(String input, String modified, String minAmount) {
        given:
          def inputData = txInputData.getString(input)
          def newMinReturn = new BigInteger(minAmount)
        when:
          def newInputData = slippageProvider.modify(inputData, newMinReturn)
        then:
          newInputData == txInputData.getString(modified)
        where:
          input      | minAmount                  | modified
          "tx1Input" | "1000"                     | "tx1Expected"
          "tx2Input" | "20000000000000"           | "tx2Expected"
          "tx3Input" | "200000000000000"          | "tx3Expected"
          "tx4Input" | "300000000000000000000000" | "tx4Expected"
    }
}
