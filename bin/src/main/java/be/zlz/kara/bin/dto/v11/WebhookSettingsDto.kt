package be.zlz.kara.bin.dto.v11

import be.zlz.kara.bin.domain.enums.Interpretation
import be.zlz.kara.bin.modules.webhook.WebhookMode
import org.springframework.http.HttpMethod

data class WebhookSettingsDto(
        val destination: String?,
        val method: HttpMethod?,
        val body: String?,
        val bodyType: Interpretation?,
        val mode: WebhookMode?,
        val auth: WebhookAuthDto?
)