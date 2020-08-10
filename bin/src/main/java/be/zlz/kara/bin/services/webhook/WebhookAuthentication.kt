package be.zlz.kara.bin.services.webhook

import be.zlz.kara.bin.domain.enums.AuthenticationType

data class WebhookAuthentication(val type: AuthenticationType, val identifier: String, val secret: String)