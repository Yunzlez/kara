package be.zlz.kara.bin.dto.v11.converter

import be.zlz.kara.bin.domain.enums.Interpretation
import be.zlz.kara.bin.dto.converter.Converter
import be.zlz.kara.bin.dto.v11.WebhookSettingsDto
import be.zlz.kara.bin.modules.webhook.WebhookSettings
import java.util.*

class WebhookSettingsConverter : Converter<WebhookSettings, WebhookSettingsDto> {
    override fun toDto(settings: WebhookSettings): WebhookSettingsDto {
        var body: String? = null
        if (settings.data != null) {
            body = if (settings.bodyType == Interpretation.TEXT) settings.data.toString(Charsets.UTF_8) else Base64.getEncoder().encodeToString(settings.data)
        }

        return WebhookSettingsDto(
                settings.destination,
                settings.method,
                body,
                settings.bodyType,
                settings.mode,
                settings.authentication
        )
    }

    override fun toDomain(u: WebhookSettingsDto): WebhookSettings {
        TODO("Not yet implemented")
    }

}