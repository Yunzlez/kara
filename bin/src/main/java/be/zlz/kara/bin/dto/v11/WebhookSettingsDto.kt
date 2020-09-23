package be.zlz.kara.bin.dto.v11

import be.zlz.kara.bin.domain.enums.Interpretation
import be.zlz.kara.bin.modules.webhook.WebhookAuthentication
import be.zlz.kara.bin.modules.webhook.WebhookMode
import be.zlz.kara.bin.modules.webhook.WebhookSettings
import org.springframework.http.HttpMethod
import java.util.*

data class WebhookSettingsDto(
        var destination: String?,
        var method: HttpMethod?,
        var body: String?,
        var bodyType: Interpretation?,
        var mode: WebhookMode?,
        var auth: WebhookAuthentication?
) {
    constructor(settings: WebhookSettings) {
        destination = settings.destination
        method = settings.method
        if (body != null) {
            body = if (settings.bodyType == Interpretation.TEXT) settings.data!!.toString(Charsets.UTF_8) else Base64.getEncoder().encodeToString(settings.data)
        }
        bodyType = settings.bodyType
        mode = settings.mode
        auth = settings.authentication
    }

}