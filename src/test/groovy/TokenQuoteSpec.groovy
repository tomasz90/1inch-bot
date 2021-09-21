import com.oneinch.config.Properties
import com.oneinch.object.TokenQuote
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration

@ContextConfiguration
@Import(TestConfig.class)
class TokenQuoteSpec extends BaseTest {

    @Autowired
    Properties properties

    def "should calculate minimum return amount of different matic token"(String fromToken,
                                                                          String toToken,
                                                                          String fromQuote,
                                                                          String minimumExpectedAmount) {
        given:
          def tokens = properties.matic.tokens
          def from = tokens.find { it.symbol == fromToken }
          def to = tokens.find { it.symbol == toToken }
          def quote = new TokenQuote(from, new BigInteger(fromQuote))
        when:
          def calculatedAmount = quote.calcMinReturnAmountOfDifferentToken(to)
        then:
          calculatedAmount == new BigInteger(minimumExpectedAmount)
        where:
          fromToken | toToken | fromQuote             | minimumExpectedAmount
          "USDC"    | "DAI"   | "1000000000000"       | "1000000000000000000000000"
          "USDC"    | "USDT"  | "1000000000000"       | "1000000000000"
          "USDC"    | "UST"   | "1000000000000"       | "1000000000000000000000000"

          "USDT"    | "DAI"   | "1000000000000"       | "1000000000000000000000000"
          "USDT"    | "USDC"  | "1000000000000"       | "1000000000000"
          "USDT"    | "UST"   | "1000000000000"       | "1000000000000000000000000"

          "UST"     | "DAI"   | "1000000000000"       | "1000000000000"
          "UST"     | "USDC"  | "1000000000000000000" | "1000000"
          "UST"     | "USDT"  | "1000000000000000000" | "1000000"

          "DAI"     | "UST"   | "1000000000000"       | "1000000000000"
          "DAI"     | "USDC"  | "1000000000000000000" | "1000000"
          "DAI"     | "USDT"  | "1000000000000000000" | "1000000"
    }

    def "should calculate minimum return amount of different bsc token"(String fromToken,
                                                                        String toToken,
                                                                        String fromQuote,
                                                                        String minimumExpectedAmount) {
        given:
          def tokens = properties.bsc.tokens
          def from = tokens.find { it.symbol == fromToken }
          def to = tokens.find { it.symbol == toToken }
          def quote = new TokenQuote(from, new BigInteger(fromQuote))
        when:
          def calculatedAmount = quote.calcMinReturnAmountOfDifferentToken(to)
        then:
          calculatedAmount == new BigInteger(minimumExpectedAmount)
        where:
          fromToken | toToken | fromQuote       | minimumExpectedAmount
          "USDC"    | "DAI"   | "1000000000000" | "1000000000000"
          "USDC"    | "USDT"  | "1000000000000" | "1000000000000"
          "USDC"    | "TUSD"  | "1000000000000" | "1000000000000"
          "USDC"    | "UST"   | "1000000000000" | "1000000000000"

          "USDT"    | "DAI"   | "1000000000000" | "1000000000000"
          "USDT"    | "TUSD"  | "1000000000000" | "1000000000000"
          "USDT"    | "USDC"  | "1000000000000" | "1000000000000"
          "USDT"    | "UST"   | "1000000000000" | "1000000000000"

          "UST"     | "DAI"   | "1000000000000" | "1000000000000"
          "UST"     | "TUSD"  | "1000000000000" | "1000000000000"
          "UST"     | "USDC"  | "1000000000000" | "1000000000000"
          "UST"     | "USDT"  | "1000000000000" | "1000000000000"

          "DAI"     | "UST"   | "1000000000000" | "1000000000000"
          "DAI"     | "TUSD"  | "1000000000000" | "1000000000000"
          "DAI"     | "USDC"  | "1000000000000" | "1000000000000"
          "DAI"     | "USDT"  | "1000000000000" | "1000000000000"

          "TUSD"    | "UST"   | "1000000000000" | "1000000000000"
          "TUSD"    | "DAI"   | "1000000000000" | "1000000000000"
          "TUSD"    | "USDC"  | "1000000000000" | "1000000000000"
          "TUSD"    | "USDT"  | "1000000000000" | "1000000000000"
    }

//
//    def "should convert origin to readable"(BigInteger origin, int decimals, double readable) {
//        given:
//            def token = new Token(symbol, address, decimals)
//            def tokenQuote = new TokenQuote(token, origin)
//        expect:
//            tokenQuote.readable == readable
//        where:
//            origin                                     | decimals || readable
//            new BigInteger("1")                        | 18       || 0.000000000000000001
//            new BigInteger("1000000")                  | 6        || 1.0
//            new BigInteger("200000000000000")          | 9        || 200000
//            new BigInteger("300000000000000000000000") | 18       || 300000.00
//    }
}
