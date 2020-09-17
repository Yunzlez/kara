package be.zlz.kara.bin.dto.v11

import be.zlz.kara.bin.domain.enums.AuthenticationType

data class WebhookAuthDto(
        val type: AuthenticationType,
        val username: String?,
        val password: String?,
        val token: String?,
        val clientId: String?,
        val secret: String?,
        val grantType: OAuth2GrantType?
)