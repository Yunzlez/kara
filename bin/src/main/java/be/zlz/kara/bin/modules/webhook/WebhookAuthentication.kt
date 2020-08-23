package be.zlz.kara.bin.modules.webhook

import be.zlz.kara.bin.domain.enums.AuthenticationType

data class WebhookAuthentication(val type: AuthenticationType, val identifier: String, val secret: String)