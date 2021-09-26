package unit

import com.oneinch.Main
import com.oneinch.api.blockchain.balance.Balance
import com.oneinch.loader.Properties
import com.oneinch.loader.Settings
import com.oneinch.object.Chain
import com.oneinch.object.Token
import com.oneinch.requester.AbstractRequester
import com.oneinch.util.RateLimiter
import kotlin.Pair
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration

import java.util.concurrent.atomic.AtomicBoolean

import static org.powermock.api.mockito.PowerMockito.mock

@ContextConfiguration
@Import(SpecConfig.class)
class MainSpec extends BaseSpec {

    @Autowired
    Properties properties

    def abstractRequester = mock(AbstractRequester)
    def balance = mock(Balance)
    def chain = mock(Chain)
    def settings = mock(Settings)
    def isSwapping = mock(AtomicBoolean)
    def rateLimiter = mock(RateLimiter)

    def main = new Main(abstractRequester, balance, chain, settings, isSwapping, rateLimiter)

    def "should create unique pairs and excluding Dai as target"() {
        given:
          def tokens = properties.chains.find { it.name == "matic" }.tokens
          def excluded = ["DAI"]

          def method = main.getClass().getDeclaredMethods().find(it -> it.name == "createUniquePairs")
          method.setAccessible(true)
        when:
          List<Pair<Token, Token>> pairs = method.invoke(main, tokens, excluded)

        then:
          StringBuilder result = new StringBuilder()
          pairs.stream().forEach(it -> result.append("${it.first.getSymbol()} ${it.second.getSymbol()}"))

          def excludedDai =
                  "DAI UST" +
                  "DAI USDC" +
                  "DAI USDT" +
                  "UST USDC" +
                  "UST USDT" +
                  "USDC UST" +
                  "USDC USDT" +
                  "USDT UST" +
                  "USDT USDC"

        result.toString() == excludedDai

    }

    def "should create unique pairs and excluding Dai and UST as target"() {
        given:
          def tokens = properties.chains.find { it.name == "matic" }.tokens
          def excluded =  ["DAI", "UST"]

          def method = main.getClass().getDeclaredMethods().find(it -> it.name == "createUniquePairs")
          method.setAccessible(true)
        when:
          List<Pair<Token, Token>> pairs = method.invoke(main, tokens, excluded)

        then:
          StringBuilder result = new StringBuilder()
          pairs.stream().forEach(it -> result.append("${it.first.getSymbol()} ${it.second.getSymbol()}"))

          def excludedDaiAndUST =
                          "DAI USDC" +
                          "DAI USDT" +
                          "UST USDC" +
                          "UST USDT" +
                          "USDC USDT" +
                          "USDT USDC"

          result.toString() == excludedDaiAndUST

    }
}
