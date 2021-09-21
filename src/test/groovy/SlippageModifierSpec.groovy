import com.oneinch.util.SlippageModifier
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration
@Import(TestConfig.class)
class SlippageModifierSpec extends BaseTest {

    @Autowired
    SlippageModifier slippageModifier

    def "should do sth"() {
        given:
          def inputData = txInputData.getString("tx1Input")
          def expectedNewData = txInputData.getString("tx1Expected")
          def newMinReturn = BigInteger.valueOf(1000)
        when:
          def newInputData = slippageModifier.modify(inputData, newMinReturn)
        then:
          newInputData == expectedNewData
    }
}
