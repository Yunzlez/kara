package be.zlz.kara.bin.services.webhook

import org.springframework.http.HttpMethod


data class WebhookContext(val destination: String, val headers: Map<String, String>, val authentication: WebhookAuthentication, val data: ByteArray, val method: HttpMethod, val isTransparent: Boolean)