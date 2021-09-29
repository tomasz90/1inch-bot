package unit.provider

import com.oneinch.api.gas_station.GasStationClient
import com.oneinch.loader.Settings
import com.oneinch.provider.GasPriceProvider
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.internal.ContextScope
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import unit.BaseSpec
import unit.SpecConfig

import static org.powermock.api.mockito.PowerMockito.mock
import static org.powermock.api.mockito.PowerMockito.when
import static org.springframework.test.util.ReflectionTestUtils.setField

@ContextConfiguration
@Import(SpecConfig.class)
class GasPriceProviderSpec extends BaseSpec {

    def scope = new ContextScope(new CoroutineName("test") as CoroutineContext)
    def settings = mock(Settings)
    def gasStationClient = mock(GasStationClient)

    def "should return gas price"() {
        given:
          when(gasStationClient.getPrice()).thenReturn(30D)

          def gasStationProvider = new GasPriceProvider(gasStationClient, settings, scope)

          setField(gasStationProvider, "gasPriceLimit", 1000D)
          setField(gasStationProvider, "TWO_SECONDS", 2L)
          sleep(200)
        expect:
          gasStationProvider.getGasPrice().get() == 30000000000L

    }

    def "should update gas price"() {
        given:
          when(gasStationClient.getPrice()).thenReturn(30D)

          def gasStationProvider = new GasPriceProvider(gasStationClient, settings, scope)

          setField(gasStationProvider, "gasPriceLimit", 1000D)
          setField(gasStationProvider, "TWO_SECONDS", 2L)
          sleep(200)

        expect:
          gasStationProvider.getGasPrice().get() == 30000000000L

        when:
          when(gasStationClient.getPrice()).thenReturn(50D)
          sleep(200)

        then:
          gasStationProvider.getGasPrice().get() == 50000000000L
    }

    def "should return limit gas price, when client data exceeds it"() {
        given:
          when(gasStationClient.getPrice()).thenReturn(3000D)

          def gasStationProvider = new GasPriceProvider(gasStationClient, settings, scope)

          setField(gasStationProvider, "gasPriceLimit", 1000D)
          setField(gasStationProvider, "TWO_SECONDS", 2L)
          sleep(200)

        expect:
          gasStationProvider.getGasPrice().get() == 1000000000000L

    }

    def "should return default gas price, when client returns null"() {
        given:
          when(gasStationClient.getPrice()).thenReturn(null)

          def gasStationProvider = new GasPriceProvider(gasStationClient, settings, scope)

          setField(gasStationProvider, "gasPriceLimit", 1000D)
          setField(gasStationProvider, "TWO_SECONDS", 2L)
          sleep(200)

        expect:
          gasStationProvider.getGasPrice().get() == 10000000000L

    }
}
