package unit.object

import com.oneinch.loader.Properties
import com.oneinch.object.Token
import com.oneinch.object.TokenQuote
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import unit.BaseTest
import unit.TestConfig

@ContextConfiguration
@Import(TestConfig.class)
class TokenQuoteSpec extends BaseTest {

    @Autowired
    Properties properties

    def "should calculate usd value properly"() {
        given:
          def token = new Token("symbol", "address", new BigDecimal("1000000000000000000"))
          def tokenQuote = new TokenQuote(token, new BigInteger("1000000000000000000000"))
        expect:
          tokenQuote.usdValue == 1000.0D
    }

    def "should calculate minimum return amount of different matic token"(String fromToken,
                                                                          String toToken,
                                                                          String fromQuote,
                                                                          String minimumExpectedAmount) {
        given:
          def tokens = properties.chains.find { it.name == "matic" }.tokens
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
          def tokens = properties.chains.find { it.name == "bsc" }.tokens
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


    def "should convert origin to readable"(BigInteger origin, double readable) {
        given:
          def tokens = properties.chains.find { it.name == "matic" }.tokens
          def token = tokens.find { it.symbol == symbol }
          def tokenQuote = new TokenQuote(token, origin)
        expect:
          tokenQuote.usdValue == readable
        where:
          symbol | origin                    | readable
          "USDC" | 10000                     | 0.01
          "USDT" | 10000                     | 0.01
          "UST"  | 10000000000000000         | 0.01
          "DAI"  | 10000000000000000         | 0.01

          "USDC" | 1000000                   | 1
          "USDT" | 1000000                   | 1
          "UST"  | 1000000000000000000       | 1
          "DAI"  | 1000000000000000000       | 1

          "USDC" | 1000000000000             | 1000000
          "USDT" | 1000000000000             | 1000000
          "UST"  | 1000000000000000000000000 | 1000000
          "DAI"  | 1000000000000000000000000 | 1000000

    }
}
