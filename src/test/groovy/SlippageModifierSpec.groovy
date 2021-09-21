import com.oneinch.util.SlippageModifier
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration
@Import(TestConfig.class)
class SlippageModifierSpec extends BaseTest {

    @Autowired
    SlippageModifier slippageModifier

    def "should modify minimum return amount"(String input, String modified, BigInteger minAmount) {
        given:
          def inputData = txInputData.getString(input)
          def newMinReturn = minAmount
        when:
          def newInputData = slippageModifier.modify(inputData, newMinReturn)
        then:
          newInputData == txInputData.getString(modified)
        where:
          input      | minAmount                                  | modified
          "tx1Input" | new BigInteger("1000")                     | "tx1Expected"
          "tx2Input" | new BigInteger("20000000000000")           | "tx2Expected"
          "tx3Input" | new BigInteger("200000000000000")          | "tx3Expected"
          "tx4Input" | new BigInteger("300000000000000000000000") | "tx4Expected"
    }
}
