package be.zlz.kara.bin.dto.v11

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EventDto (
        val id: String?,
        val method: String?,
        val source: String?,
        val location: String?,
        val metadata: Map<String, String>?,
        val additionalData: Map<String, String>?,
        val contentType: String?,
        val eventType: LocalDateTime?,
        val origin: String?,
        val bodySize: Long?,
        val protocolVersion: String?,
        val body: String?
)