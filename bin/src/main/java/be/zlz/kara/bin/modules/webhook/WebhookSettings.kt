package be.zlz.kara.bin.modules.webhook

import org.springframework.http.HttpMethod


data class WebhookSettings(val destination: String, val headers: Map<String, String>, val authentication: WebhookAuthentication, val data: ByteArray, val method: HttpMethod, val isTransparent: Boolean, val binId: Long)