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

    def USD_90 = new BigInteger("90000000000000000000")
    def USD_300 = new BigInteger("300000000000000000000")
    def USD_700 = new BigInteger("700000000000000000000")

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
    Method swapWhenNotExceedingMaximalShare
    Token token

    def setup() {
        main = new Main(scope, abstractRequester, balance, chain, settings, isSwapping, rateLimiter)
        main = spy(main)
        swap = mock(Function3)
        setField(main, "swap", swap)

        createUniquePairs = main.getClass().getDeclaredMethods().find(it -> it.name == "createUniquePairs")
        createUniquePairs.setAccessible(true)

        swapWhenMoreThanMinimalQuote = main.getClass().getDeclaredMethods()
                .find(it -> it.name == "swapWhenMoreThanMinimalQuote")
        swapWhenMoreThanMinimalQuote.setAccessible(true)

        swapWhenNotExceedingMaximalShare = main.getClass().getDeclaredMethods()
                .find(it -> it.name == "swapWhenNotExceedingMaximalShare")
        swapWhenNotExceedingMaximalShare.setAccessible(true)

        // settings:
        setField(settings, "minSwapQuote", 100) // minimal quote to swap
        setField(settings, "maximalTokenShare", 0.6D) // max 60% of sharing
        when(balance.getUsdValue()).thenReturn(1000D) // all balance 1000 USD
        token = new Token("symbol", "address", new BigDecimal("1000000000000000000"))
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
          def excluded = ["DAI", "UST"]

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
          def tokenQuote = new TokenQuote(token, USD_90)

        when:
          swapWhenMoreThanMinimalQuote.invoke(main, tokenQuote, token)

        then:
          verifyPrivate(swap, never()).invoke("invoke", any(), any(), any())
    }

    def "Should not swap when too big share in all balance"() {
        given:
          def tokenQuote = new TokenQuote(token, USD_300)
          def tokenQuote2 = new TokenQuote(token, USD_700)
          when(balance.getERC20(token)).thenReturn(tokenQuote2)

        when:
          swapWhenMoreThanMinimalQuote.invoke(main, tokenQuote, token)

        then:
          verifyPrivate(balance).invoke("getERC20", token)
          verifyPrivate(swap, never()).invoke("invoke", any(), any(), any())

    }

    def "Should swap when destination tokenQuote is null - balance is 0"() {
        given:
          def tokenQuote = new TokenQuote(token, USD_300)
          when(balance.getERC20(token)).thenReturn(null)

        when:
          swapWhenMoreThanMinimalQuote.invoke(main, tokenQuote, token)

        then:
          verifyPrivate(swap).invoke("invoke", any(), any(), any())

    }

    def "should swap all TokenQuote balance, when tokenShare is null and it doesn't exceeds max share value"() {
        given:
          def tokenQuote = new TokenQuote(token, USD_300)
          def tokenShare = null
          def maxShare = 600.0D

        when:
          def result = new MainTest(settings).swapOnlyToMaximalShare(tokenQuote, token, tokenShare, maxShare)

        then:
          result.usdValue >= settings.minSwapQuote
          result.usdValue <= tokenQuote.usdValue
          shouldReturnExactQuoteWhenNotRandom(result.usdValue, tokenQuote.usdValue)
    }

    def "should swap all TokenQuote balance, when tokenShare is small and sum will not exceeds max share value"() {
        given:
          def tokenQuote = new TokenQuote(token, USD_300)
          def tokenShare = 150.0D
          def maxShare = 600.0D

        when:
          def result = new MainTest(settings).swapOnlyToMaximalShare(tokenQuote, token, tokenShare, maxShare)

        then:
          result.usdValue >= settings.minSwapQuote
          result.usdValue <= 300.0D
          shouldReturnExactQuoteWhenNotRandom(result.usdValue, tokenQuote.usdValue)

    }

    def "should swap part of TokenQuote balance matching max, when tokenShare is null and TokenQuote exceeds max share value"() {
        given:
          def tokenQuote = new TokenQuote(token, USD_700)
          def tokenShare = null
          def maxShare = 600.0D

        when:
          def result = new MainTest(settings).swapOnlyToMaximalShare(tokenQuote, token, tokenShare, maxShare)

        then:
          result.usdValue >= settings.minSwapQuote
          result.usdValue <= 600.0D
          shouldReturnExactQuoteWhenNotRandom(result.usdValue, tokenQuote.usdValue)
    }

    def "should swap part of TokenQuote balance, when sum of token and TokenQuote would exceeds max share"() {
        given:
          def tokenQuote = new TokenQuote(token, USD_700)
          def tokenShare = 200.0D
          def maxShare = 600.0D

        when:
          def result = new MainTest(settings).swapOnlyToMaximalShare(tokenQuote, token, tokenShare, maxShare)

        then:
          result.usdValue >= settings.minSwapQuote
          result.usdValue <= 400.0D
          shouldReturnExactQuoteWhenNotRandom(result.usdValue, tokenQuote.usdValue)

    }


    static verifyCreatingUniquePairs(pairs) {
        StringBuilder result = new StringBuilder()
        pairs.stream().forEach(it -> result.append("${it.first.getSymbol()} ${it.second.getSymbol()}"))
        return result.toString()
    }

    def shouldReturnExactQuoteWhenNotRandom(double result, double expect) {
        if (!settings.randomSwapQuote) {
            return result == expect
        } else true
    }
}
