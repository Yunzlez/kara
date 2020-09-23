package be.zlz.kara.bin.modules.webhook

import be.zlz.kara.bin.domain.enums.AuthenticationType
import be.zlz.kara.bin.dto.v11.OAuth2GrantType

data class WebhookAuthentication(
        val type: AuthenticationType,
        val username: String?,
        val password: String?,
        val token: String?,
        val clientId: String?,
        val secret: String?,
        val grantType: OAuth2GrantType?
)