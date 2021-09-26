package unit.requester

import com.oneinch.api.blockchain.sender.Sender
import com.oneinch.api.blockchain.tx.Transaction
import com.oneinch.api.blockchain.tx.TransactionCreator
import com.oneinch.api.one_inch.OneInchClient
import com.oneinch.api.one_inch.api.data.SwapDto
import com.oneinch.api.one_inch.api.data.Tx
import com.oneinch.loader.Properties
import com.oneinch.loader.Settings
import com.oneinch.object.Token
import com.oneinch.object.TokenQuote
import com.oneinch.provider.AdvantageProvider
import com.oneinch.requester.Requester
import com.oneinch.util.Utils
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import org.powermock.reflect.Whitebox
import org.spockframework.spring.EnableSharedInjection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared
import unit.BaseSpec
import unit.SpecConfig

import java.util.concurrent.atomic.AtomicBoolean

import static org.mockito.Mockito.never
import static org.mockito.Mockito.verify
import static org.powermock.api.mockito.PowerMockito.mock
import static org.powermock.api.mockito.PowerMockito.when
import static org.springframework.test.util.ReflectionTestUtils.setField

@ContextConfiguration
@EnableSharedInjection
@Import(SpecConfig.class)
class RequesterSpec extends BaseSpec {

    //todo removed prepare for test from test classes

    @Shared
    @Autowired
    Properties properties

    static def sender = mock(Sender)
    static def transactionCreator = mock(TransactionCreator)
    static def settings = mock(Settings)
    static def oneInchClient = mock(OneInchClient)
    static def utils = mock(Utils)
    static def advantageProvider = mock(AdvantageProvider)
    static def isSwapping = mock(AtomicBoolean)
    static Token token1
    static Token token2
    def tx = mock(Tx)
    def transaction = mock(Transaction)

    static requester = new Requester(sender, transactionCreator, settings)

    def setup() {
        Whitebox.setInternalState(advantageProvider, "advantage", 0.2D)
        setField(requester, "oneInchClient", oneInchClient)
        setField(requester, "protocols", "protocols")
        setField(requester, "utils", utils)
        setField(requester, "advantageProvider", advantageProvider)
        setField(requester, "isSwapping", isSwapping)

        def tokens = properties.chains.find { it.name == "matic" }.tokens
        token1 = tokens.find { it.symbol == "USDC" }
        token2 = tokens.find { it.symbol == "USDT" }
    }

    def "should swap tokens when all conditions are met"() {
        given:
          def tokenQuote1 = new TokenQuote(token1, BigInteger.valueOf(1_000_000_000L))
          def tokenQuote2 = new TokenQuote(token2, BigInteger.valueOf(1_020_000_000L))
          def swapDto = new SwapDto(tokenQuote1, tokenQuote2, tx)
          when(oneInchClient.swap(tokenQuote1, token2, false, "protocols")).thenReturn(swapDto)
          def continuation = Mock(Continuation) { getContext() >> Mock(CoroutineContext) }
        when:
          requester.swap(tokenQuote1, token2, continuation)
        then:
          verify(isSwapping).set(true)
          verify(isSwapping).set(false)

    }

    def "should not swap tokens when advantage is too low"() {
        given:
          def tokenQuote1 = new TokenQuote(token1, BigInteger.valueOf(1_000_000_000L))
          def tokenQuote2 = new TokenQuote(token2, BigInteger.valueOf(999_000_000L))
          def swapDto = new SwapDto(tokenQuote1, tokenQuote2, tx)
          when(oneInchClient.swap(tokenQuote1, token2, false, "protocols")).thenReturn(swapDto)
          def continuation = Mock(Continuation) { getContext() >> Mock(CoroutineContext) }
        when:
          requester.swap(tokenQuote1, token2, continuation)
        then:
          verify(isSwapping, never()).set(true)
          verify(isSwapping, never()).set(false)

    }
}
