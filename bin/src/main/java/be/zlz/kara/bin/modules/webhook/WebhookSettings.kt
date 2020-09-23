package be.zlz.kara.bin.modules.webhook

import be.zlz.kara.bin.domain.enums.Interpretation
import org.springframework.http.HttpMethod


data class WebhookSettings(
        val destination: String,
        val headers: Map<String, String>,
        val authentication: WebhookAuthentication,
        val data: ByteArray?,
        val method: HttpMethod,
        val bodyType: Interpretation?,
        val mode: WebhookMode,
        val binId: Long
)