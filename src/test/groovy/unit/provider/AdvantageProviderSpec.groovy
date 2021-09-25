package unit.provider

import com.oneinch.loader.Settings
import com.oneinch.provider.AdvantageProvider
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import unit.BaseSpec
import unit.SpecConfig

import static java.time.Duration.ofSeconds
import static java.time.Instant.now
import static org.springframework.test.util.ReflectionTestUtils.setField

@ContextConfiguration
@Import(SpecConfig.class)
class AdvantageProviderSpec extends BaseSpec {

    def settings = GroovyMock(Settings)
    def defaultAdvantage = 0.5D

    def "should set advantage based on settings"() {
        given:
          setField(settings, "minAdvantage", defaultAdvantage)

          def advantageProvider = new AdvantageProvider(settings)
          setField(advantageProvider, "HALF_HOUR", 1L)

        expect:
          advantageProvider.advantage == defaultAdvantage
    }

    def "should set advantage to 0.0 when no transaction in x hours"() {
        given:
          setField(settings, "minAdvantage", defaultAdvantage)

          def crossedDeadline = now() - ofSeconds(5)
          def advantageProvider = new AdvantageProvider(settings)
          setField(advantageProvider, "HALF_HOUR", 1L)
          setField(advantageProvider, "deadline", crossedDeadline)
        expect:
          Thread.sleep(100)
          advantageProvider.advantage == 0.0D
    }

    def "should not set advantage to 0.0 when deadline not achived"() {
        given:
          setField(settings, "minAdvantage", 0.5D)

          def crossedDeadline = now() + ofSeconds(5)
          def advantageProvider = new AdvantageProvider(settings)
          setField(advantageProvider, "HALF_HOUR", 1L)
          setField(advantageProvider, "deadline", crossedDeadline)
        expect:
          Thread.sleep(100)
          advantageProvider.advantage == defaultAdvantage
    }
}
