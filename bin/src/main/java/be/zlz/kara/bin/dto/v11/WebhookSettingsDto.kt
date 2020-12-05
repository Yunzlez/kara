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

}