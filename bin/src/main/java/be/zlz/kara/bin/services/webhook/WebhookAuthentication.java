package be.zlz.kara.bin.services.webhook;

import be.zlz.kara.bin.domain.enums.AuthenticationType;

public class WebhookAuthentication {

    private AuthenticationType type;

    private String identifier;

    private String secret;

    public WebhookAuthentication(AuthenticationType type, String identifier, String secret) {
        this.type = type;
        this.identifier = identifier;
        this.secret = secret;
    }

    public AuthenticationType getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getSecret() {
        return secret;
    }
}
