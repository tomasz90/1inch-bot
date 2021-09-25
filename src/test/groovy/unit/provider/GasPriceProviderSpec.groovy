package unit.provider

import com.oneinch.api.gas_station.GasStationClient
import com.oneinch.loader.Settings
import com.oneinch.provider.GasPriceProvider
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import unit.BaseSpec
import unit.SpecConfig

import static org.springframework.test.util.ReflectionTestUtils.setField

@ContextConfiguration
@Import(SpecConfig.class)
class GasPriceProviderSpec extends BaseSpec {

    Settings settings = GroovyMock()

    def "should return gas price"() {
        given:
          GasStationClient gasStationClient = GroovyMock() { getPrice() >> 20.0D }
          setField(settings, "gasPriceLimit", 30)
          def gasPriceProvider = new GasPriceProvider(gasStationClient, settings)


        expect:
          Thread.sleep(1000)
          gasPriceProvider.gasPrice.get() == 20
    }
}
