package be.zlz.kara.bin.modules

import be.zlz.kara.bin.domain.Event
import be.zlz.kara.bin.domain.Reply
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

abstract class KaraModule(
        val key: String
) {
    protected val om = jacksonObjectMapper()

    abstract fun handleEventSync(config: String, event: Event): Reply?

    abstract fun handleEventAsync(config: String, event: Event)
}