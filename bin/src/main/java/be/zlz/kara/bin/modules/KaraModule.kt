package be.zlz.kara.bin.modules

import be.zlz.kara.bin.domain.Event
import be.zlz.kara.bin.domain.Reply
import be.zlz.kara.bin.domain.Response
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

abstract class KaraModule(
        val key: String
) {
    protected val om = jacksonObjectMapper()

    abstract fun handleEventSync(config: String, event: Event): Response?

    abstract fun handleEventAsync(config: String, event: Event)
}