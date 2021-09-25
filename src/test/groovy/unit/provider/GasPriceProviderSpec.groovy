package unit.provider

import com.oneinch.St
import com.oneinch.Tes
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import unit.BaseSpec
import unit.SpecConfig

@PrepareForTest([Tes.class])
@ContextConfiguration
@Import(SpecConfig.class)
class GasPriceProviderSpec extends BaseSpec {


    def "should return gas price"() {
        given:
          Tes tes = PowerMockito.mock(Tes)
          PowerMockito.when(tes.foo()).thenReturn("20.0D")
          def st = new St(tes)
        expect:
          st.fizz() == "20.D"

    }
}
