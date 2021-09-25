package unit.provider

import com.oneinch.loader.Settings
import com.oneinch.provider.AdvantageProvider
import org.springframework.context.annotation.Import
import org.springframework.test.context.ContextConfiguration
import unit.BaseTest
import unit.TestConfig

import static java.time.Duration.ofSeconds
import static java.time.Instant.now
import static org.springframework.test.util.ReflectionTestUtils.setField

@ContextConfiguration
@Import(TestConfig.class)
class AdvantageProviderSpec extends BaseTest {

    def settings = GroovyMock(Settings)


    def "should set advantage based on settings"() {
        given:
          setField(settings, "minAdvantage", 0.5D)
          def advantageProvider = new AdvantageProvider(settings)
        expect:
          advantageProvider.advantage == 0.5D
    }

    def "should set advantage to 0.0 when no transaction in x hours"() {
        given:
          def hours = 2
          def crossedDeadline = now() - ofSeconds(5)
          setField(settings, "minAdvantage", 0.5D)
          setField(settings, "maxTimeNoTransaction", hours)

          def advantageProvider = new AdvantageProvider(settings)
          setField(advantageProvider, "HALF_HOUR", 1L)
          setField(advantageProvider, "deadline", crossedDeadline)
        expect:
          Thread.sleep(10)
          advantageProvider.advantage == 0.0D
    }
}
