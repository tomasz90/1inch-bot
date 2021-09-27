package com.oneinch.provider.advantage

import com.oneinch.loader.Settings
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("fakeAccount")
class FakeAdvantageProvider(val settings: Settings) : IAdvantageProvider {

    override var advantage = settings.minAdvantage
}