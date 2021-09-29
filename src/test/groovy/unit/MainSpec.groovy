package unit

import com.oneinch.Main
import com.oneinch.api.blockchain.balance.Balance
import com.oneinch.loader.Properties
import com.oneinch.loader.Settings
import com.oneinch.object.Chain
import com.oneinch.object.Token
import com.oneinch.object.TokenQuote
import com.oneinch.requester.AbstractRequester
import com.oneinch.util.RateLimiter
import kotlin.Pair
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.functions.Function3
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.internal.ContextScope
import org.spockframework.spring.EnableSharedInjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.concurrent.atomic.AtomicBoolean

import static org.mockito.Mockito.any
import static org.mockito.Mockito.never
import static org.powermock.api.mockito.PowerMockito.mock
import static org.powermock.api.mockito.PowerMockito.spy
import static org.powermock.api.mockito.PowerMockito.verifyPrivate
import static org.powermock.api.mockito.PowerMockito.when
import static org.springframework.test.util.ReflectionTestUtils.setField

@ContextConfiguration
@EnableSharedInjection
@Import([SpecConfig.class])
class MainSpec extends BaseSpec {

    @Autowired
    Properties properties

    @Shared
    @Autowired
    Settings settings

    def scope = new ContextScope(new CoroutineName("test") as CoroutineContext)
    def abstractRequester = mock(AbstractRequester)
    def balance = mock(Balance)
    def chain = mock(Chain)
    def isSwapping = mock(AtomicBoolean)
    def rateLimiter = mock(RateLimiter)
    Main main
    Function3 swap
    Method createUniquePairs
    Method swapWhenMoreThanMinimalQuote

    def setup() {
        main = new Main(scope, abstractRequester, balance, chain, settings, isSwapping, rateLimiter)
        main = spy(main)
        swap = mock(Function3)
        setField(main, "swap", swap)
        setField(settings, "minSwapQuote", 100)

        createUniquePairs = main.getClass().getDeclaredMethods().find(it -> it.name == "createUniquePairs")
        createUniquePairs.setAccessible(true)

        swapWhenMoreThanMinimalQuote = main.getClass()
                .getDeclaredMethods().find(it -> it.name == "swapWhenMoreThanMinimalQuote")
        swapWhenMoreThanMinimalQuote.setAccessible(true)
    }

    def "should create unique pairs and excluding Dai as target"() {
        given:
          def tokens = properties.chains.find { it.name == "matic" }.tokens
          def excluded = ["DAI"]

        when:
          List<Pair<Token, Token>> pairs = createUniquePairs.invoke(main, tokens, excluded) as List<Pair<Token, Token>>

        then:
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

          verifyCreatingUniquePairs(pairs) == excludedDai

    }

    def "should create unique pairs and excluding Dai and UST as target"() {
        given:
          def tokens = properties.chains.find { it.name == "matic" }.tokens
          def excluded =  ["DAI", "UST"]

        when:
          List<Pair<Token, Token>> pairs = createUniquePairs.invoke(main, tokens, excluded) as List<Pair<Token, Token>>

        then:
          def excludedDaiAndUST =
                          "DAI USDC" +
                          "DAI USDT" +
                          "UST USDC" +
                          "UST USDT" +
                          "USDC USDT" +
                          "USDT USDC"

          verifyCreatingUniquePairs(pairs) == excludedDaiAndUST

    }

    def "Should not swap below minimal quote"() {
        given:
          def token1 = new Token("symbol", "address", new BigDecimal("1000000000000000000"))
          def tokenQuote = new TokenQuote(token1, new BigInteger("90000000000000000000")) // 90 USD
          def token2 = new Token("symbol", "address", new BigDecimal("1000000000000000000"))
          def method = main.getClass().getDeclaredMethods().find(it -> it.name == "swapWhenMoreThanMinimalQuote")
          method.setAccessible(true)

        when:
          swapWhenMoreThanMinimalQuote.invoke(main, tokenQuote, token2)

        then:
          verifyPrivate(balance, never()).invoke("getERC20", token2)
    }

    def "Should not swap when too big share in all balance"() {
        given:
          def token1 = new Token("symbol", "address", new BigDecimal("1000000000000000000"))
          def tokenQuote = new TokenQuote(token1, new BigInteger("150000000000000000000")) // 150 USD
          setField(settings, "maximalTokenShare", 0.6D)// max 240 USD in share

          def token2 = new Token("symbol", "address", new BigDecimal("1000000000000000000"))
          def tokenQuote2 = new TokenQuote(token1, new BigInteger("250000000000000000000")) // 250 USD
          when(balance.getERC20(token2)).thenReturn(tokenQuote2)
          when(balance.getUsdValue()).thenReturn(400D)

        when:
          swapWhenMoreThanMinimalQuote.invoke(main, tokenQuote, token2)

        then:
          verifyPrivate(balance).invoke("getERC20", token2)
    }

    def "Should swap when destination tokenQuote is null - balance is 0"() {
        given:
          def token1 = new Token("symbol", "address", new BigDecimal("1000000000000000000"))
          def tokenQuote = new TokenQuote(token1, new BigInteger("150000000000000000000")) // 150 USD
          setField(settings, "maximalTokenShare", 0.6D)// max 240 USD in share

          def token2 = new Token("symbol", "address", new BigDecimal("1000000000000000000"))
          when(balance.getERC20(token2)).thenReturn(null)
          when(balance.getUsdValue()).thenReturn(400D)

        when:
          swapWhenMoreThanMinimalQuote.invoke(main, tokenQuote, token2)

        then:
          verifyPrivate(swap).invoke("invoke", any(), any(), any())

    }

    def "should swap"() {
        given:
          def token1 = new Token("symbol", "address", new BigDecimal("1000000000000000000"))
          def tokenQuote = new TokenQuote(token1, new BigInteger("150000000000000000000")) // 150 USD
          def maxShare = 600

          def token2 = new Token("symbol", "address", new BigDecimal("1000000000000000000"))
          def tokenShare2 = 100.0D

          def method = main.getClass().getDeclaredMethods().find(it -> it.name == "swapOnlyToMaximalShare")
          method.setAccessible(true)

        when:
          method.invoke(main, tokenQuote, token2, tokenShare2, maxShare)

        then:
          verifyPrivate(swap).invoke("invoke", any(), any(), any())
    }


    static verifyCreatingUniquePairs(pairs) {
        StringBuilder result = new StringBuilder()
        pairs.stream().forEach(it -> result.append("${it.first.getSymbol()} ${it.second.getSymbol()}"))
        return result.toString()
    }
}
