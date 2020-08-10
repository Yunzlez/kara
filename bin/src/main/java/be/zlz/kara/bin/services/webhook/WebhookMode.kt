package be.zlz.kara.bin.services.webhook

enum class WebhookMode {
    PROXY,  //pass-through
    INFO,  //send request information
    CUSTOM
}
