package be.zlz.kara.bin.dto.v11

data class BinSettingsDto(
        val name: String,
        val isPermanent: Boolean,
        val reply: ReplySettingsDto,
        val webhook: WebhookSettingsDto
)