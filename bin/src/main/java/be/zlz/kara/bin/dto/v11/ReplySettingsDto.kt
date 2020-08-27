package be.zlz.kara.bin.dto.v11

data class ReplySettingsDto(
        val code: Int,
        val contentType: String,
        val body: String,
        val headers: Map<String, String>,
        val responseType: Interpretation,
        val resposneOrigin: ResponseOrigin
)