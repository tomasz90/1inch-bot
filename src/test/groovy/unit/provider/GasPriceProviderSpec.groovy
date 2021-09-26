package unit.provider

import com.oneinch.api.gas_station.GasStationClient
import com.oneinch.loader.Settings
import com.oneinch.provider.GasPriceProvider
import org.powermock.core.classloader.annotations.PrepareForTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import unit.BaseSpec
import unit.SpecConfig

import static org.powermock.api.mockito.PowerMockito.mock
import static org.powermock.api.mockito.PowerMockito.when
import static org.springframework.test.util.ReflectionTestUtils.setField

@PrepareForTest([GasStationClient.class, Settings.class])
@ContextConfiguration
@Import(SpecConfig.class)
class GasPriceProviderSpec extends BaseSpec {

    def settings = mock(Settings)
    def gasStationClient = mock(GasStationClient)

    def "should return gas price"() {
        given:
          when(gasStationClient.getPrice()).thenReturn(30D)

          def gasStationProvider = new GasPriceProvider(gasStationClient, settings)

          setField(gasStationProvider, "gasPriceLimit", 1000D)
          setField(gasStationProvider, "TWO_SECONDS", 2L)
          Thread.sleep(200)
        expect:
          gasStationProvider.getGasPrice().get() == 30000000000L

    }

    def "should update gas price"() {
        given:
          when(gasStationClient.getPrice()).thenReturn(30D)

          def gasStationProvider = new GasPriceProvider(gasStationClient, settings)

          setField(gasStationProvider, "gasPriceLimit", 1000D)
          setField(gasStationProvider, "TWO_SECONDS", 2L)
          Thread.sleep(200)
        expect:
          gasStationProvider.getGasPrice().get() == 30000000000L

        when:
          when(gasStationClient.getPrice()).thenReturn(50D)
          Thread.sleep(200)

        then:
          gasStationProvider.getGasPrice().get() == 50000000000L


    }
}
